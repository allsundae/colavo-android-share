package com.colavo.android.ui.customerdetail

import com.colavo.android.base.BaseView
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.event.EventModel

interface CustomerDetailListView : BaseView {

    fun setCustomerDetaillist(customerDetailEntities: List<CustomerDetailModel>?)

    fun showReceiptFragment(eventModel: EventModel)

    fun showMemoFragment(eventModel: EventModel)

    fun addCustomerDetail(customerDetailEntity: CustomerDetailModel)

    fun changeCustomerDetail(customerDetailEntity: CustomerDetailModel)

    fun removeCustomerDetail(customerDetailEntity: CustomerDetailModel)

    fun showProgress()

    fun hideProgress()

}