package com.colavo.android.ui.checkout

import com.colavo.android.base.BaseView
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.event.EventModel

interface CheckoutDetailListView : BaseView {

    fun setCheckoutDetaillist(checkoutEntities: List<CheckoutModel>?)

    fun showEventDetailFragment(eventModel: EventModel)

    fun showProgress()

    fun hideProgress()

}