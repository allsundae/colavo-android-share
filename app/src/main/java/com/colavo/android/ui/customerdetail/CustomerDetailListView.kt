package com.colavo.android.ui.customerdetail

import com.colavo.android.base.BaseView
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.event.EventModel

interface CustomerDetailListView : BaseView {

    fun setCustomerDetaillist(customerDetailEntities: List<CheckoutModel>?)

    fun showReceiptFragment(eventModel: EventModel)

    fun showMemoFragment(eventModel: EventModel)

    fun addCustomerDetail(customerDetailEntity: CheckoutModel)

    fun changeCustomerDetail(customerDetailEntity: CheckoutModel)

    fun removeCustomerDetail(customerDetailEntity: CheckoutModel)

    fun showProgress()

    fun hideProgress()

}