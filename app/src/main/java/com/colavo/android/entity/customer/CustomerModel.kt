package com.colavo.android.entity.customer

import java.io.Serializable

class CustomerModel(key: String = "",
                    phone: String = "",
                    national_phone: String = "",
                    name: String ="",
                    image_url:ImageUrl = ImageUrl("",""),
                    recent_appointment_begin_at: String ="",
                    recent_appointment_end_at: String ="",
                    is_removed: Boolean = false,
                    fund: Double = 0.0
                    )
    : BaseCustomer(key, phone, national_phone, name, image_url , recent_appointment_begin_at, recent_appointment_end_at, is_removed, fund), Serializable

