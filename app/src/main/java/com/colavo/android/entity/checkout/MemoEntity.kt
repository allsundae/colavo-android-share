package com.colavo.android.entity.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rx.schedulers.TimeInterval
import java.io.Serializable
import java.util.*


class  MemoEntity(
        @SerializedName("txt")                @Expose var txt: String = "",
        @SerializedName("image_urls")         @Expose var image_urls: HashMap<String, Memo_Image>
                                                                 = hashMapOf("" to Memo_Image(0.0,"","","")),
        @SerializedName("created_at")         @Expose var created_at: Double = 0.0,
        @SerializedName("updated_at")         @Expose var updated_at: Double = 0.0,
        @SerializedName("uid")                @Expose var uid: String = "",
        @SerializedName("salon_key")          @Expose var salon_key: String = "",
        @SerializedName("employee_key")       @Expose var employee_key: String = "",
        @SerializedName("event_key")          @Expose var event_key: String = ""

):  Serializable


data class Memo_Image (
        @SerializedName("created_at")            @Expose var created_at: Double = 0.0,
        @SerializedName("full")                  @Expose var full: String = "",
        @SerializedName("thumb")                 @Expose var thumb: String = "",
        @SerializedName("key")                   @Expose var key: String = ""
): Serializable
