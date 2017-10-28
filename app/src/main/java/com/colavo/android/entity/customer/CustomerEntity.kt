package com.colavo.android.entity.customer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CustomerEntity(
        uid: String = "",
        @SerializedName("phone")                        @Expose override var phone: String = "",
        @SerializedName("national_phone")               @Expose override var national_phone: String = "",
        @SerializedName("name")                         @Expose override var name: String = "",
        @SerializedName("image")                        @Expose override var image: String = "",
        @SerializedName("recent_appointment_begin_at")  @Expose override var recent_appointment_begin_at: String = "",
        @SerializedName("recent_appointment_end_at")    @Expose override var recent_appointment_end_at: String = "",
        @SerializedName("is_removed")                   @Expose override var is_removed: Boolean = false,
        @SerializedName("fund")                         @Expose override var fund: Double = 0.0)
    : BaseCustomer(uid, phone, national_phone, name, image
                , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable