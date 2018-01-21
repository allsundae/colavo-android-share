package com.colavo.android.presenters.customerdetail

import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.customerdetail.CustomerCreateView
import com.colavo.android.ui.login.LoginView

interface CustomerCreatePresenter : BasePresenter {

    fun attachView(createCustomerView: CustomerCreateView)

    fun createCustomer(salonKey: String, name: String, phone: String, imageUrl: ImageUrl)

    fun showCreateCustomerImage() //TODO

}