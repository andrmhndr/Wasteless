package com.example.wasteless.ui.user

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wasteless.R
import com.example.wasteless.adapter.OrderAdapter
import com.example.wasteless.model.OrderModel
import com.example.wasteless.ui.DetailActivity
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var mAuth: FirebaseAuth

    private lateinit var rvOrder: RecyclerView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtRole: TextView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var role: String
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mAuth = Firebase.auth

        rvOrder = findViewById(R.id.rv_order)
        txtName = findViewById(R.id.txt_name)
        txtEmail = findViewById(R.id.txt_email)
        txtRole = findViewById(R.id.txt_role)
        btnAdd = findViewById(R.id.btn_add)

        rvOrder.setHasFixedSize(true)
        rvOrder.layoutManager = LinearLayoutManager(this)


        userViewModel.getUser(mAuth.currentUser).observe(this, { user ->
            txtName.text = user.name
            txtEmail.text = user.email
            txtRole.text = user.role
            role = user.role!!
        })

        /*GlobalScope.launch(Dispatchers.IO){
            userViewModel.setOrderList(mAuth.currentUser)
            withContext(Dispatchers.Main){
                showList(userViewModel.getOrderList())
            }
        }*/

        /*userViewModel.getOrderList(mAuth.currentUser).observe(this, {orderData ->
            if (orderData.isNotEmpty()){
                showList(orderData as ArrayList<OrderModel>)
            }
        })*/

        btnAdd.setOnClickListener {
            val goAdd = Intent(this@UserActivity, AddOrderActivity::class.java)
            startActivity(goAdd)
        }

    }

    private fun showList(orderModelData: ArrayList<OrderModel>){
        orderAdapter = OrderAdapter(orderModelData, role)
        orderAdapter.notifyDataSetChanged()
        rvOrder.adapter = orderAdapter

        orderAdapter.setOnItemClickCallback(object: OrderAdapter.OnItemClickCallback{
            override fun onItemClicked(data: OrderModel) {
                val goDetail = Intent(this@UserActivity, DetailActivity::class.java)
                goDetail.putExtra(Helper.ID, data.id)
                goDetail.putExtra(Helper.ROLE, role)
                goDetail.putExtra(Helper.DATE, Helper.dateToString(data.date))
                goDetail.putExtra(Helper.UID, data.id)
                goDetail.putExtra(Helper.NAME, data.stakeholderName)
                goDetail.putExtra(Helper.PHONE, data.stakeholderPhone)
                goDetail.putExtra(Helper.ADDRESS, data.stakeholderAddress)
                goDetail.putExtra(Helper.INFO, data.info)
                startActivity(goDetail)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            userViewModel.setOrderList(mAuth.currentUser)
            withContext(Dispatchers.Main){
                showList(userViewModel.getOrderList())
            }
        }
    }
}