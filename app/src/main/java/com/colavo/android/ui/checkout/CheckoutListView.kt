package com.colavo.android.ui.checkout

import com.colavo.android.base.BaseView
import com.colavo.android.entity.checkout.CheckoutModel


interface CheckoutListView : BaseView {

    fun setCheckoutlist(customerEntities: List<CheckoutModel>?)

    fun showCreateCheckoutlistFragment()

    fun openCheckoutFragment(customerModel: CheckoutModel)

    fun addCheckout(customerEntity: CheckoutModel)

    fun changeCheckout(customerEntity: CheckoutModel)

    fun removeCheckout(customerEntity: CheckoutModel)

    fun showProgress()

    fun hideProgress()

}