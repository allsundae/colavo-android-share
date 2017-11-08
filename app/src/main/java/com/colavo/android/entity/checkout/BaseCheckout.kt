package com.colavo.android.entity.checkout

import java.io.Serializable
import java.net.URL
import java.sql.Timestamp


open abstract class BaseCheckout(
        var uid: String,
        open var salon_key: String,                 //required,,,,
        open var event_key: String,                //required
        open var price: Double,                //required,,,,,,,,,
        open var is_manual_price: Boolean,
        open var reserve_fund: Double,
        open var paid_fund: Double,
        open var author_employee_key: String,
        open var paid_types: List<PaidType>,    //required
        open var created_at: String,         //required
        open var updated_at: String,              //required
        open var reservedFund: Double,
        open var paidFund: Double,
        open var tip: Double
)