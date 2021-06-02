package com.example.wasteless.ui.stakeholder

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wasteless.R
import com.example.wasteless.adapter.OrderAdapter
import com.example.wasteless.model.OrderModel
import com.example.wasteless.ui.DetailActivity
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.StakeholderViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StakeholderActivity : AppCompatActivity() {

    private lateinit var stakeholderViewModel: StakeholderViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var orderAdapter: OrderAdapter

    private lateinit var rvOrder: RecyclerView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtRole: TextView
    private lateinit var btnScan: FloatingActionButton
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stakeholder)

        stakeholderViewModel = ViewModelProvider(this).get(StakeholderViewModel::class.java)

        mAuth = Firebase.auth

        rvOrder = findViewById(R.id.rv_order)
        txtName = findViewById(R.id.txt_name)
        txtEmail = findViewById(R.id.txt_email)
        txtRole = findViewById(R.id.txt_role)
        btnScan = findViewById(R.id.btn_scan)

        rvOrder.setHasFixedSize(true)
        rvOrder.layoutManager = LinearLayoutManager(this)

        stakeholderViewModel.getStakeholder(mAuth.currentUser).observe(this, {stakeholder ->
            txtName.text = stakeholder.name
            txtEmail.text = stakeholder.email
            txtRole.text = stakeholder.role
            role = stakeholder.role!!
        })

        /*GlobalScope.launch(Dispatchers.IO){
            stakeholderViewModel.setOrderList(mAuth.currentUser)
            withContext(Dispatchers.Main){
                showList(stakeholderViewModel.getOrderList())
            }
        }*/

        /*stakeholderViewModel.getOrderList(mAuth.currentUser).observe(this, {orderData ->
            Toast.makeText(this, txtName.text, Toast.LENGTH_SHORT).show()
            showList(orderData)
            rvOrder.adapter?.notifyDataSetChanged()
        })*/

        btnScan.setOnClickListener {  }

    }

    private fun showList(orderModelData: ArrayList<OrderModel>) {

        orderAdapter = OrderAdapter(orderModelData, role)
        orderAdapter.notifyDataSetChanged()
        rvOrder.adapter = orderAdapter

        orderAdapter.setOnItemClickCallback(object: OrderAdapter.OnItemClickCallback{
            override fun onItemClicked(data: OrderModel) {
                val goDetail = Intent(this@StakeholderActivity, DetailActivity::class.java)
                goDetail.putExtra(Helper.ID, data.id)
                goDetail.putExtra(Helper.ROLE, role)
                goDetail.putExtra(Helper.UID, data.id)
                goDetail.putExtra(Helper.NAME, data.userName)
                goDetail.putExtra(Helper.PHONE, data.userPhone)
                goDetail.putExtra(Helper.ADDRESS, data.userAddress)
                goDetail.putExtra(Helper.INFO, data.info)
                startActivity(goDetail)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            stakeholderViewModel.setOrderList(mAuth.currentUser)
            withContext(Dispatchers.Main){
                showList(stakeholderViewModel.getOrderList())
            }
        }
    }
}