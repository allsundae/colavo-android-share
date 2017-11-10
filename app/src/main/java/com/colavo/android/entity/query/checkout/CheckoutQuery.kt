package com.colavo.android.entity.query.checkout

import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.entity.query.BaseQuery


sealed class CheckoutQuery : BaseQuery {
    class GetCheckout(val salonUid: String) : CheckoutQuery()
    class CreateCheckout(
                         val checkout_uid: String
                         , val salon_key: String
                         , val event_key: String
                         , val price: Double
                         , val is_manual_price: Boolean
                         , val reserve_fund: Double
                         , val paid_fund: Double
                         , val author_employee_key: String
                         , val paid_types: List<PaidType>
                         , val created_at: Double
                         , val updated_at: Double
                         , val reserveFund: Double
                         , val paidFund: Double
                         , val tip: Double
    ) : CheckoutQuery()
}