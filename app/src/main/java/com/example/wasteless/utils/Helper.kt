package com.example.wasteless.utils

import android.os.Looper
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.*

class Helper {

    companion object{

        const val ORDER = "order"
        const val RC_SIGN_IN = 101
        const val ACCOUNT = "account"
        const val USER = "user"
        const val STAKEHOLDER = "stakeholder"
        const val REGISTER = "register"
        const val NAME = "name"
        const val ADDRESS = "address"
        const val PHONE = "phone"
        const val ROLE = "role"
        const val UID = "uid"
        const val EMAIL = "email"

        const val ID = "id"
        const val USERUID = "useruid"
        const val USERPHONE = "userphone"
        const val STAKEHOLDERUID = "stakeholderuid"
        const val DATE = "date"
        const val INFO = "info"
        const val USERADDRESS = "useraddress"
        const val STAKEHOLDERADDRESS = "stakeholderaddress"
        const val USERNAME = "username"
        const val STAKEHOLDERPHONE = "stakeholderphone"
        const val STAKEHOLDERNAME = "stakeholdername"

        const val FINISHED = "finished"
        const val PROCESS = "process"
        const val CANCELLED = "cancelled"

        val handler = Handler(Looper.getMainLooper())
        val SERVICE_LATENCY_IN_MILLIS: Long = 2000

        fun dateToString(date: Date?): String{
            val dateFormat = SimpleDateFormat("dd - MM - yyyy")
            return dateFormat.format(date)
        }
    }

}