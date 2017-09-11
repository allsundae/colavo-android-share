package com.colavo.android.entity.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by RUS on 23.07.2016.
 */
data class EventEntity(var id: String = "",
                       @SerializedName("salonId") @Expose val salonId: String = "",
                       @SerializedName("userId") @Expose var userId: String = "",
                       @SerializedName("text") @Expose val text: String = "",
                       @SerializedName("time") @Expose val time: String = "")