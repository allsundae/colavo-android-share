package com.colavo.android.entity.customer

import java.io.Serializable

class CustomerModel(uid: String = "",
                    phone: String = "",
                    national_phone: String = "",
                    name: String ="",
           //         image_full_url: String ="",
            //        image_thumb_url: String ="",
                    image_urls:List<ImageUrl> = mutableListOf(ImageUrl("","")),
                    recent_appointment_begin_at: String ="",
                    recent_appointment_end_at: String ="",
                    is_removed: Boolean = false,
                    fund: Double = 0.0
                    )
    : BaseCustomer(uid, phone, national_phone, name, image_urls , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable

