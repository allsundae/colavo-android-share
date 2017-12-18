package com.colavo.android.presenters.customerdetail

import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.customerdetail.CustomerDetailListView


interface CustomerDetailPresenter : BasePresenter {

    fun attachView(customerDetailListView: CustomerDetailListView)

    fun initialize(customerUid: String)

    override fun onDestroy()
}