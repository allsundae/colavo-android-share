package com.colavo.android.entity.checkout

import java.io.Serializable

class CheckoutModel(checkout_uid: String = "",
                    salon_key: String ="",
                    event_key: String = "",
                    price: String ="",
                    is_manual_price: Boolean = false,
                    reserve_fund: String ="",
                    paid_fund: String ="",
                    author_employee_key: String = "",
                    paid_types: List<PaidType> = mutableListOf(PaidType("","","", "", "")),
                    created_at: String = "",
                    updated_at: String = "",
                    reserveFund: String ="",
                    paidFund: String ="",
                    tip: String =""
                    )
    : BaseCheckout(checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                    ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reserveFund
                    ,paidFund, tip), Serializable

