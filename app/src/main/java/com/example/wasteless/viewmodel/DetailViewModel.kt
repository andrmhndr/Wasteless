package com.example.wasteless.viewmodel

import androidx.lifecycle.ViewModel
import com.example.wasteless.model.DetailModel
import com.example.wasteless.model.OrderModel
import com.example.wasteless.utils.Helper
import java.util.*

class DetailViewModel: ViewModel() {
    private lateinit var detailData: DetailModel

    fun setDetailData(role: String?, id: String?, uId: String?, name: String?, phone: String?, address: String?, date: String?, info: String?){
        detailData = DetailModel(
           id,
           role,
           uId,
           name,
           phone,
           address,
           date,
           info
        )
    }

    fun getDetailData(): DetailModel{
        return detailData
    }

}