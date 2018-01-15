package com.colavo.android.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FirebaseResponse(@SerializedName("name") @Expose val id: String)