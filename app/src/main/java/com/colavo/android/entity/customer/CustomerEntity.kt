package com.colavo.android.entity.customer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CustomerEntity(
        @SerializedName("key")                          @Expose override var key: String = "", //international number,
        @SerializedName("phone")                        @Expose override var phone: String = "", //international number
        @SerializedName("national_phone")               @Expose override var national_phone: String = "", //local number
        @SerializedName("name")                         @Expose override var name: String = "",
        @SerializedName("image_url")                    @Expose override var image_url: ImageUrl  = ImageUrl("",""),
        @SerializedName("recent_appointment_begin_at")  @Expose override var recent_appointment_begin_at: String = "",
        @SerializedName("recent_appointment_end_at")    @Expose override var recent_appointment_end_at: String = "",
        @SerializedName("is_removed")                   @Expose override var is_removed: Boolean = false,
        @SerializedName("fund")                         @Expose override var fund: Double = 0.0
)   : BaseCustomer(key, phone, national_phone, name, image_url
                , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable {


   //    fun getImageUrl() : ImageUrl = image_url
/*fun setImageUrl(full:String, thumb:String){
        this.image_url.full = full
        this.image_url.thumb = thumb
    }*/
}


data class ImageUrl (
        @SerializedName("full") @Expose var full: String = "",
        @SerializedName("thumb") @Expose var thumb: String = ""
        ) : Serializable


