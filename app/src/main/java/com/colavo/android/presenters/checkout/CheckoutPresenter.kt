package com.colavo.android.presenters.checkout

import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.checkout.CheckoutListView


interface CheckoutPresenter : BasePresenter {

    fun attachView(checkoutlistView: CheckoutListView)

    fun initialize(checkoutId: String)

    fun onCreateCheckoutButtonClicked()

    fun createCheckout(
              uid: String
            , salon_key: String
            , event_key: String
            , price: Double
            , is_manual_price: Boolean
            , reserve_fund: Double
            , paid_fund: Double
            , author_employee_key: String
            , paid_types: List<PaidType>
            , created_at: Double
            , updated_at: Double
            , reservedFund: Double
            , paidFund: Double
            , tip: Double
    )

    override fun onDestroy()
}