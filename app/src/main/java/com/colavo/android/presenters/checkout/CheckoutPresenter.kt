package com.colavo.android.presenters.checkout

import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.checkout.CheckoutlistView


interface CheckoutPresenter : BasePresenter {

    fun attachView(checkoutlistView: CheckoutlistView)

    fun initialize(checkoutId: String)

    fun onCreateCheckoutButtonClicked()

    fun createCheckout(
              uid: String
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
    )

    override fun onDestroy()
}