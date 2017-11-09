package com.colavo.android.interactors.checkout

import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.repositories.checkout.CheckoutRepository
import com.colavo.android.entity.response.FirebaseResponse
import rx.Subscriber
import javax.inject.Inject

//TODO Checkout
class CreateCheckout @Inject constructor(checkoutRepository: CheckoutRepository) : CheckoutUseCase(checkoutRepository) {

    fun execute(
            checkout_uid: String
            , salon_key: String
            , event_key: String
            , price: String
            , is_manual_price: Boolean
            , reserve_fund: String
            , paid_fund: String
            , author_employee_key: String
            , paid_types: List<PaidType>
            , created_at: String
            , updated_at: String
            , reservedFund: String
            , paidFund: String
            , tip: String
                , subscriber: Subscriber<FirebaseResponse>)
            = super.execute(CheckoutQuery.CreateCheckout(
            checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                        ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reservedFund
                        ,paidFund, tip ), subscriber)

}