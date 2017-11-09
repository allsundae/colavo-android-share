package com.colavo.android.entity.checkout

import java.io.Serializable
import java.net.URL
import java.sql.Timestamp


open abstract class BaseCheckout(
        var checkout_uid: String,
        open var salon_key: String,                 //required,,,,
        open var event_key: String,                //required
        open var price: String,                //required,,,,,,,,,
        open var is_manual_price: Boolean,
        open var reserve_fund: String,
        open var paid_fund: String,
        open var author_employee_key: String,
        open var paid_types: List<PaidType>,    //required
        open var created_at: String,         //required
        open var updated_at: String,              //required
        open var reserveFund: String,
        open var paidFund: String,
        open var tip: String
)