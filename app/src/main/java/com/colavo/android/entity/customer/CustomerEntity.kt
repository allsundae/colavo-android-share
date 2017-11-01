package com.colavo.android.entity.customer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


class CustomerEntity(
        uid: String = "",
        @SerializedName("phone")                        @Expose override var phone: String = "",
        @SerializedName("national_phone")               @Expose override var national_phone: String = "",
        @SerializedName("name")                         @Expose override var name: String = "",
   //     @SerializedName("image_url/full")               @Expose override var image_full_url: String = "",
   //     @SerializedName("image_url/thumb")              @Expose override var image_thumb_url: String = "",
        @SerializedName("image_url")                    @Expose override var image_urls: List<ImageUrl> = mutableListOf(ImageUrl("","")),
       // @SerializedName("image_url")                    @Expose override var image_urls: ImageUrl? = null,
        @SerializedName("recent_appointment_begin_at")  @Expose override var recent_appointment_begin_at: String = "",
        @SerializedName("recent_appointment_end_at")    @Expose override var recent_appointment_end_at: String = "",
        @SerializedName("is_removed")                   @Expose override var is_removed: Boolean = false,
        @SerializedName("fund")                         @Expose override var fund: Double = 0.0)
    : BaseCustomer(uid, phone, national_phone, name, image_urls
                , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable

/*        public ImageUrl(image_full_url: String, image_thumb_url: String) {
            this.image_full_url = image_full_url
            this.image_thumb_url = image_thumb_url
        }*/




data class ImageUrl (
        @SerializedName("full") @Expose var image_full_url: String = "",
        @SerializedName("thumb") @Expose var image_thumb_url: String = ""
        )


/*
public class ImageUrl {
    @SerializedName("full")   @Expose var image_full_url: String = ""
    @SerializedName("thumb")  @Expose var image_thumb_url: String = ""

    fun getFullUrl(): String {
        return image_full_url
    }

    fun getThumbUrl(): String {
        return image_thumb_url
    }
}*/
