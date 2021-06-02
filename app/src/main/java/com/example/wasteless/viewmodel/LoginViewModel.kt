package com.example.wasteless.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wasteless.utils.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var role: MutableLiveData<String> = MutableLiveData<String>()

    fun check(currentUser:FirebaseUser?): LiveData<String> {
        currentUser?.uid?.let {
            db.collection(Helper.ACCOUNT).document(currentUser.uid).get().addOnSuccessListener { document->
                role.value = document?.getString(Helper.ROLE)
            }
        }
        return role
    }

}