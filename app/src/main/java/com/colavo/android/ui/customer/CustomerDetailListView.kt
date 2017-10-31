package com.colavo.android.ui.customer

import com.colavo.android.base.BaseView
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.event.EventModel

interface CustomerDetailListView : BaseView {

    fun setCustomerDetaillist(customerEntities: List<CustomerModel>?)

    fun showEventDetailFragment(eventModel: EventModel)

    fun showProgress()

    fun hideProgress()

}