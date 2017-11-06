package com.colavo.android.ui.customer

import com.colavo.android.base.BaseView
import com.colavo.android.entity.customer.CustomerModel


interface CustomerlistView : BaseView {

    fun setCustomerlist(customerEntities: List<CustomerModel>?)

    fun showCreateCustomerlistFragment()

    fun openCustomerFragment(customerModel: CustomerModel)

    fun addCustomer(customerEntity: CustomerModel)

    fun changeCustomer(customerEntity: CustomerModel)

    fun removeCustomer(customerEntity: CustomerModel)

    fun showProgress()

    fun hideProgress()

}