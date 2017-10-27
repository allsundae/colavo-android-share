package com.colavo.android.di.customer

import com.colavo.android.ui.PlaceholderFragment04
import com.colavo.android.ui.customer.CustomerListActivity
import dagger.Subcomponent

@CustomerScope
@Subcomponent(modules = arrayOf(CustomerModule::class))
interface CustomerComponent {
    fun inject(placeholderFragment04: CustomerListActivity)  //TODO WTF
}