package com.colavo.android.entity.checkout

import java.io.Serializable
import java.net.URL
import java.sql.Timestamp


open abstract class BaseCheckout(
        var checkout_uid: String,
        open var salon_key: String,                 //required,,,,
        open var event_key: String,                //required
        open var price: Double,                //required,,,,,,,,,
        open var is_manual_price: Boolean,
        open var reserve_fund: Double,
        open var paid_fund: Double,
        open var author_employee_key: String,
        open var paid_types: List<PaidType>,    //required
        open var created_at: Double,         //required
        open var updated_at: Double,              //required
        open var reserveFund: Double,
        open var paidFund: Double,
        open var tip: Double,
        open var customer_key: String,
        open var customer_name: String,
        open var customer_image_thumb: String,
        open var customer_image_full: String,
        open var customer_menu: String
)