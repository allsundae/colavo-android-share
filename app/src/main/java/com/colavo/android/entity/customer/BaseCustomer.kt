package com.colavo.android.entity.customer

import java.io.Serializable
import java.net.URL
import java.sql.Timestamp


open abstract class BaseCustomer(
        var uid: String,
        open var phone: String,
        open var national_phone: String,
        open var name: String,
     //   open var image_full_url: String,
      //  open var image_thumb_url: String,
        open var image_urls: List<ImageUrl>,// ImageUrl?,
        open var recent_appointment_begin_at: String,
        open var recent_appointment_end_at: String,
        open var is_removed: Boolean,
        open var fund: Double
                                ) : Serializable