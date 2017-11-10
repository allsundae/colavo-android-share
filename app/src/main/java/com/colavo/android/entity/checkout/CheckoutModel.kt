package com.colavo.android.entity.checkout

import java.io.Serializable

class CheckoutModel(checkout_uid: String = "",
                    salon_key: String ="",
                    event_key: String = "",
                    price: Double = 0.0,
                    is_manual_price: Boolean = false,
                    reserve_fund: Double = 0.0,
                    paid_fund: Double = 0.0,
                    author_employee_key: String = "",
                    paid_types: List<PaidType> = mutableListOf(PaidType("","","", 0.0, 0.0)),
                    created_at: Double = 0.0,
                    updated_at: Double = 0.0,
                    reserveFund: Double = 0.0,
                    paidFund: Double = 0.0,
                    tip: Double = 0.0
                    )
    : BaseCheckout(checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                    ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reserveFund
                    ,paidFund, tip), Serializable

