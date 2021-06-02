package com.example.wasteless.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wasteless.R
import com.example.wasteless.ui.stakeholder.StakeholderActivity
import com.example.wasteless.ui.user.UserActivity
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = Firebase.auth

        progressBar = findViewById(R.id.progress)
        progressView = findViewById(R.id.view_progress)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin = findViewById(R.id.btn_login)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            progressView.visibility = View.VISIBLE
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Helper.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Helper.RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    loginViewModel.check(mAuth.currentUser).observe(this,{role ->
                        if (role == null){
                            updateUI(Helper.REGISTER)
                        } else {
                            updateUI(role)
                        }
                    })
                } else {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(role: String?) {
        when {
            role.equals(Helper.STAKEHOLDER) -> {
                Toast.makeText(this, R.string.login_stakeholder, Toast.LENGTH_SHORT).show()
                val goStakeholder = Intent(this@LoginActivity, StakeholderActivity::class.java)
                startActivity(goStakeholder)
                finish()
            }
            role.equals(Helper.USER) -> {
                Toast.makeText(this, R.string.login_user, Toast.LENGTH_SHORT).show()
                val goUser = Intent(this@LoginActivity, UserActivity::class.java)
                startActivity(goUser)
                finish()
            }
            role.equals(Helper.REGISTER) -> {
                Toast.makeText(this, R.string.register, Toast.LENGTH_SHORT).show()
                val goRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(goRegister)
                finish()
            }
            else -> {
                Toast.makeText(this, R.string.login_first, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null){
            loginViewModel.check(mAuth.currentUser).observe(this, {role ->
                updateUI(role)
            })
        } else{
            updateUI(null)
            progressBar.visibility = View.INVISIBLE
            progressView.visibility = View.INVISIBLE
        }
    }

}