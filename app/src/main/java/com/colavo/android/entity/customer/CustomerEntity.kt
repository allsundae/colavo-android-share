package com.colavo.android.entity.customer

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
class CustomerEntity(
        uid: String = "",
        @SerializedName("phone")                        @Expose override var phone: String = "",
        @SerializedName("national_phone")               @Expose override var national_phone: String = "",
        @SerializedName("name")                         @Expose override var name: String = "",
        @SerializedName("image_url")                    @Expose override var image_urls: ImageUrl  = ImageUrl("",""),
        @SerializedName("recent_appointment_begin_at")  @Expose override var recent_appointment_begin_at: String = "",
        @SerializedName("recent_appointment_end_at")    @Expose override var recent_appointment_end_at: String = "",
        @SerializedName("is_removed")                   @Expose override var is_removed: Boolean = false,
        @SerializedName("fund")                         @Expose override var fund: Double = 0.0
)   : BaseCustomer(uid, phone, national_phone, name, image_urls
                , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable {

/*
       fun getImageUrl() : ImageUrl {
        return image_urls
    }
fun setImageUrl(full:String, thumb:String){
        this.image_urls.full = full
        this.image_urls.thumb = thumb
    }*/
}


data class ImageUrl (
        @SerializedName("full") @Expose var full: String = "",
        @SerializedName("thumb") @Expose var thumb: String = ""
        ) : Serializable {
}


