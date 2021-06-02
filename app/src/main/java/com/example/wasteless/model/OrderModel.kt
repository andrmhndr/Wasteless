package com.example.wasteless.model

import java.util.*

data class OrderModel(
    var id: String? = null,
    var date: Date? = null,
    var userUid: String? = null,
    var userName: String? = null,
    var userPhone: String? = null,
    var userAddress: String? = null,
    var stakeholderUid: String? = null,
    var stakeholderName: String? = null,
    var stakeholderPhone: String? = null,
    var stakeholderAddress: String? = null,
    var info: String? = null
    )
