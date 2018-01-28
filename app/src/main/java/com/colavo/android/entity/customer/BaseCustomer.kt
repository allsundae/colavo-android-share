package com.colavo.android.entity.customer

import java.io.Serializable
import java.net.URL
import java.sql.Timestamp


abstract class BaseCustomer(
        var uid: String,
        open var phone: String,
        open var national_phone: String,
        open var name: String,
     //   open var full: String,
      //  open var thumb: String,
        open var image_url: ImageUrl,// ImageUrl?,
        open var recent_appointment_begin_at: String,
        open var recent_appointment_end_at: String,
        open var is_removed: Boolean,
        open var fund: Double
                                ) : Serializable