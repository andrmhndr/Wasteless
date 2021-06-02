package com.example.wasteless.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wasteless.R
import com.example.wasteless.ui.stakeholder.StakeholderActivity
import com.example.wasteless.ui.user.UserActivity
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnCreate: Button
    private lateinit var radioG: RadioGroup

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = Firebase.auth
        etName = findViewById(R.id.txt_name)
        etAddress = findViewById(R.id.txt_address)
        etPhone = findViewById(R.id.txt_phone)
        btnCreate = findViewById(R.id.btn_create)
        radioG = findViewById(R.id.radioG)

        currentUser = mAuth.currentUser

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        radioG.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio.id){
                R.id.rd_user -> {
                    role = Helper.USER
                }
                R.id.rd_stakeholder -> {
                    role = Helper.STAKEHOLDER
                }
            }
        }

        btnCreate.setOnClickListener {
            if (etName.text.isNullOrEmpty() || etAddress.text.isNullOrEmpty() || etPhone.text.isNullOrEmpty() || role.isEmpty()){
                Toast.makeText(this, R.string.required, Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.adduser(currentUser, etName.text.toString(), role, etAddress.text.toString() , etPhone.text.toString()).observe(this, {info ->
                    if (info){
                        Toast.makeText(this, R.string.create_success, Toast.LENGTH_SHORT).show()
                        updateUi(role)
                    }
                })
            }
        }
    }

    private fun updateUi(role:String){
        when(role){
            Helper.USER -> {
                val goUser = Intent(this, UserActivity::class.java)
                startActivity(goUser)
                finish()
            }
            Helper.STAKEHOLDER -> {
                val goStakeholder = Intent(this, StakeholderActivity::class.java)
                startActivity(goStakeholder)
                finish()
            }
        }
    }
}