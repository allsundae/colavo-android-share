package com.colavo.android.di.customer

import com.colavo.android.ui.customer.CustomerListActivity
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(CustomerModule::class))
@CustomerScope
interface CustomerComponent {

    fun inject(customerlistActivity: CustomerListActivity)  //TODO WTF

}