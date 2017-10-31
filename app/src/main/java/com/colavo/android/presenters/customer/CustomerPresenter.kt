package com.colavo.android.presenters.customer

import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.customer.CustomerlistView


interface CustomerPresenter : BasePresenter {

    fun attachView(customerlistView: CustomerlistView)

    fun initialize(customerId: String)

    fun onCreateCustomerButtonClicked()

    fun createCustomer(customerUid: String, customerPhone: String, customerName: String, customerImageUrl: ImageUrl?)

    override fun onDestroy()
}