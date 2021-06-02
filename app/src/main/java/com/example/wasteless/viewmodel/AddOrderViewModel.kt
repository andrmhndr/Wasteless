package com.example.wasteless.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wasteless.model.UserModel
import com.example.wasteless.utils.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class AddOrderViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var userData: UserModel
    //private lateinit var userModelList: MutableLiveData<ArrayList<UserModel>>
    private lateinit var userModelList: ArrayList<UserModel>

    suspend fun setUserList(){
        userModelList = arrayListOf()
        db.collection(Helper.ACCOUNT).whereEqualTo(Helper.ROLE, Helper.STAKEHOLDER).get().addOnSuccessListener { document ->
            if (document != null){
                document.forEach { data ->
                    val userModel = UserModel(
                        data.getString(Helper.UID),
                        data.getString(Helper.EMAIL),
                        data.getString(Helper.NAME),
                        data.getString(Helper.ROLE),
                        data.getString(Helper.ADDRESS),
                        data.getString(Helper.PHONE)
                    )
                    userModelList.add(userModel)
                }
            }
        }.await()
    }

    fun getUserList(): ArrayList<UserModel>{
        return userModelList
    }

    /*fun getUserList(): LiveData<ArrayList<UserModel>> {
        userModelList = MutableLiveData<ArrayList<UserModel>>()
        setUserList()
        return userModelList
    }

    private fun setUserList(){
        Helper.handler.postDelayed({
            val userList: ArrayList<UserModel> = arrayListOf()
            db.collection(Helper.ACCOUNT).whereEqualTo(Helper.ROLE, Helper.STAKEHOLDER).get().addOnSuccessListener { document ->
                if (document != null){
                    document.forEach { data ->
                        val userModel = UserModel(
                            data.getString(Helper.UID),
                            data.getString(Helper.EMAIL),
                            data.getString(Helper.NAME),
                            data.getString(Helper.ROLE),
                            data.getString(Helper.ADDRESS),
                            data.getString(Helper.PHONE)
                        )
                        userList.add(userModel)
                    }
                }
            }
            userModelList.value?.addAll(userList)
        }, Helper.SERVICE_LATENCY_IN_MILLIS)
    }*/

    suspend fun getUser(currentUser: FirebaseUser?) {
        currentUser?.uid?.let {
            db.collection(Helper.ACCOUNT).document(it).get().addOnSuccessListener { document ->
                if (document != null) {
                    userData = UserModel(
                        document.getString(Helper.UID),
                        document.getString(Helper.EMAIL),
                        document.getString(Helper.NAME),
                        document.getString(Helper.ROLE),
                        document.getString(Helper.ADDRESS),
                        document.getString(Helper.PHONE)
                    )
                }
            }.await()
        }
    }

    fun setOrder(stakeholderData:UserModel){
        val order = hashMapOf(
            Helper.DATE to Calendar.getInstance().time,
            Helper.USERUID to userData.uid,
            Helper.USERNAME to userData.name,
            Helper.USERPHONE to userData.phone,
            Helper.USERADDRESS to userData.address,
            Helper.STAKEHOLDERUID to stakeholderData.uid,
            Helper.STAKEHOLDERNAME to stakeholderData.name,
            Helper.STAKEHOLDERPHONE to stakeholderData.phone,
            Helper.STAKEHOLDERADDRESS to stakeholderData.address,
            Helper.INFO to Helper.PROCESS
        )
        db.collection(Helper.ORDER).add(order)
    }

}