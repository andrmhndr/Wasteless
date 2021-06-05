package com.example.wasteless.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wasteless.model.OrderModel
import com.example.wasteless.model.UserModel
import com.example.wasteless.utils.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class StakeholderViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var stakeholderData: MutableLiveData<UserModel> = MutableLiveData<UserModel>()
    /*private var orderModelList: MutableLiveData<ArrayList<OrderModel>> = MutableLiveData<ArrayList<OrderModel>>()*/
    private lateinit var orderModelList: ArrayList<OrderModel>

    fun getStakeholder(currentUser: FirebaseUser?): LiveData<UserModel> {
        currentUser?.uid?.let {
            db.collection(Helper.ACCOUNT).document(it).get().addOnSuccessListener { document ->
                if (document != null) {
                    stakeholderData.value = UserModel(
                        document.getString(Helper.UID),
                        document.getString(Helper.EMAIL),
                        document.getString(Helper.NAME),
                        document.getString(Helper.ROLE),
                        document.getString(Helper.ADDRESS),
                        document.getString(Helper.PHONE)
                    )
                }
            }
        }
        return stakeholderData
    }

    suspend fun setOrderList(currentUser: FirebaseUser?){
        orderModelList = arrayListOf()
        currentUser?.uid?.let {
            db.collection(Helper.ORDER).orderBy(Helper.DATE, Query.Direction.DESCENDING).get().addOnSuccessListener{ document ->
                document?.forEach { data ->
                    if (data.getString(Helper.STAKEHOLDERUID) == currentUser.uid){
                        val order = OrderModel(
                            data.id,
                            data.getDate(Helper.DATE),
                            data.getString(Helper.USERUID),
                            data.getString(Helper.USERNAME),
                            data.getString(Helper.USERPHONE),
                            data.getString(Helper.USERADDRESS),
                            data.getString(Helper.STAKEHOLDERUID),
                            data.getString(Helper.STAKEHOLDERNAME),
                            data.getString(Helper.STAKEHOLDERPHONE),
                            data.getString(Helper.STAKEHOLDERADDRESS),
                            data.getString(Helper.INFO))
                        orderModelList.add(order)
                    }
                }
            }.await()
        }
    }

    fun getOrderList(): ArrayList<OrderModel> {
        return orderModelList
    }

    /*fun getOrderList(currentUser: FirebaseUser?): LiveData<ArrayList<OrderModel>>{
        currentUser?.uid?.let {
            orderModelList.value?.clear()
            db.collection(Helper.ORDER).whereEqualTo(Helper.STAKEHOLDERUID, currentUser.uid).orderBy(Helper.DATE).get().addOnSuccessListener { document ->
                document?.forEach { data ->
                    val order = OrderModel(
                        data.getDate(Helper.DATE),
                        data.getString(Helper.USERUID),
                        data.getString(Helper.USERNAME),
                        data.getString(Helper.USERADDRESS),
                        data.getString(Helper.STAKEHOLDERUID),
                        data.getString(Helper.STAKEHOLDERNAME),
                        data.getString(Helper.STAKEHOLDERADDRESS),
                        data.getString(Helper.INFO))
                    orderModelList.value?.add(order)
                }
            }
        }
        return orderModelList
    }*/

}