package com.colavo.android.entity.checkout

import java.io.Serializable
import java.util.*

class CheckoutModel(
        id: String = "",
        created_at: Double = 0.0,
        updated_at: Double = 0.0,
        begin_at: Double = 0.0,
        end_at: Double = 0.0,
        employee_only_event_title: String = "",
        booked_by_customer: Boolean = false,
        salon_key: String = "",
        employee_key: String = "",
        customer_key: String = "",
        memo_key: String = "",
        checkout_key: String = "",
        cancel_reason: String = "",

        var services: HashMap<String, ServiceMenu>  = hashMapOf("" to ServiceMenu(
                0.0,0.0,0.0,"", "", 0.0, "", "", 0.0)),
        var discounts: HashMap<String, DiscountMenu> = hashMapOf("" to DiscountMenu("",false,"","",0.0,0.0, 0.0)),
        var logs: HashMap<String, Boolean> = hashMapOf("" to false),

        var customer_name: String ="",
        var customer_image_thumb: String="",
        var customer_image_full: String="",
        var customer_phone: String="",
        var customer_fund: Double=0.0,
        var service_menus: String="",

        var memo_txt: String="",
        var checkout_price: String="",
        var checkout_paid_type: String=""
                    )
    : BaseCheckout(id, created_at, updated_at, begin_at, end_at
        , employee_only_event_title
        , booked_by_customer, salon_key, employee_key, customer_key
        , memo_key, checkout_key, cancel_reason), Serializable

