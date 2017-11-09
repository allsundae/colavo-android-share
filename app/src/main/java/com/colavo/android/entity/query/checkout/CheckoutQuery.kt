package com.colavo.android.entity.query.checkout

import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.entity.query.BaseQuery


sealed class CheckoutQuery : BaseQuery {
    class GetCheckout(val salonUid: String) : CheckoutQuery()
    class CreateCheckout(
                         val checkout_uid: String
                         , val salon_key: String
                         , val event_key: String
                         , val price: String
                         , val is_manual_price: Boolean
                         , val reserve_fund: String
                         , val paid_fund: String
                         , val author_employee_key: String
                         , val paid_types: List<PaidType>
                         , val created_at: String
                         , val updated_at: String
                         , val reserveFund: String
                         , val paidFund: String
                         , val tip: String
    ) : CheckoutQuery()
}