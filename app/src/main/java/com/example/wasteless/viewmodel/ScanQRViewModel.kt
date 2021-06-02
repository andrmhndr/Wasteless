package com.example.wasteless.viewmodel

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.wasteless.utils.Helper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope

class ScanQRViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var id: String
    private lateinit var uid: String

    private fun devideString(result: String){
        val arrayResult = result.split("*")
        id = arrayResult[0]
        uid = arrayResult[1]
    }

    fun process(result: String){
        devideString(result)
        db.collection(Helper.ORDER).document(id).get().addOnSuccessListener { document ->
            if (document.getString(Helper.STAKEHOLDERUID) == uid){
                val data = hashMapOf(
                    Helper.INFO to Helper.FINISHED
                )
                db.collection(Helper.ORDER).document(id).set(data, SetOptions.merge()).addOnSuccessListener {
                    Log.e(TAG, "success")
                }
            } else {
                Log.e(TAG,"$id Wrong $result QR Code $uid")
            }
        }
    }
}