package com.example.wasteless.ui.user

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wasteless.R
import com.example.wasteless.adapter.UserAdapter
import com.example.wasteless.model.UserModel
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.AddOrderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddOrderActivity : AppCompatActivity() {

    private lateinit var addOrderViewModel: AddOrderViewModel
    private lateinit var rvUser: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        mAuth = Firebase.auth

        addOrderViewModel = ViewModelProvider(this).get(AddOrderViewModel::class.java)
        rvUser = findViewById(R.id.rv_user)
        progressBar = findViewById(R.id.progress)


        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(this)
        /*addOrderViewModel.getUserList().observe(this, {userData ->
            progressBar.visibility = View.INVISIBLE
            showList(userData)
            if (userData.isNotEmpty()){
            }
        })*/
        GlobalScope.launch(Dispatchers.IO){
            addOrderViewModel.setUserList()
            withContext(Dispatchers.Main){
                showList(addOrderViewModel.getUserList())
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun showList(userData: ArrayList<UserModel>) {
        val userAdapter = UserAdapter(userData)
        rvUser.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserModel) {
                GlobalScope.launch(Dispatchers.IO){
                    addOrderViewModel.getUser(mAuth.currentUser)
                    withContext(Dispatchers.Main){
                        addOrderViewModel.setOrder(data)
                        finish()
                    }
                }
            }
        })

    }
}