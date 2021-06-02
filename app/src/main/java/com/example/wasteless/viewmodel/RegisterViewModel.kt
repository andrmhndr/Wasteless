package com.example.wasteless.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wasteless.utils.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var info: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun adduser(currentUser: FirebaseUser?, name: String, role:String, address: String, phone: String): LiveData<Boolean>{
        if (currentUser != null) {
            val user = hashMapOf(
                Helper.UID to currentUser.uid,
                Helper.EMAIL to currentUser.email,
                Helper.NAME to name,
                Helper.ROLE to role,
                Helper.ADDRESS to address,
                Helper.PHONE to phone
            )
            db.collection(Helper.ACCOUNT).document(currentUser.uid).set(user).addOnSuccessListener {
                info.value = true
            }
        }
        return info
    }
}