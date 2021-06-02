package com.example.wasteless.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.wasteless.R
import com.example.wasteless.utils.Helper
import com.example.wasteless.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var imageQR: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtInfo: TextView
    private lateinit var txtDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageQR = findViewById(R.id.image_qr)
        txtName = findViewById(R.id.txt_name)
        txtPhone = findViewById(R.id.txt_phone)
        txtAddress = findViewById(R.id.txt_address)
        txtInfo = findViewById(R.id.txt_info)
        txtDate = findViewById(R.id.txt_date)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        detailViewModel.setDetailData(
            intent.getStringExtra(Helper.ROLE),
            intent.getStringExtra(Helper.ID),
            intent.getStringExtra(Helper.UID),
            intent.getStringExtra(Helper.NAME),
            intent.getStringExtra(Helper.PHONE),
            intent.getStringExtra(Helper.ADDRESS),
            intent.getStringExtra(Helper.DATE),
            intent.getStringExtra(Helper.INFO)
        )

        txtDate.text = detailViewModel.getDetailData().date
        txtName.text = detailViewModel.getDetailData().name
        txtPhone.text = detailViewModel.getDetailData().phone
        txtAddress.text = detailViewModel.getDetailData().address
        txtInfo.text = detailViewModel.getDetailData().info

        if (detailViewModel.getDetailData().role == Helper.STAKEHOLDER){
            imageQR.visibility = View.INVISIBLE
        } else {

        }

    }
}