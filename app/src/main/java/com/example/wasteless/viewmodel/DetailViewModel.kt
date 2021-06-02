package com.example.wasteless.viewmodel

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.ViewModel
import com.example.wasteless.model.OrderModel
import com.example.wasteless.utils.Helper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.WriterException
import java.util.*

data class DetailModel(
    var id: String? = null,
    var role: String? = null,
    var uid: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var date: String? = null,
    var info: String? = null
)

class DetailViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
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

    fun getQRCode(): Bitmap? {
        val qrData = "${detailData.id}*${detailData.uid}"
        val qrgEncoder = QRGEncoder(qrData, null, QRGContents.Type.TEXT, 500)
        return qrgEncoder.bitmap
    }

    fun deleteOrder(){
        db.collection(Helper.ORDER).document(detailData.id!!).delete()
    }

}