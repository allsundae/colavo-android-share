package com.colavo.android.di.customer

import com.colavo.android.ui.PlaceholderFragment04
import com.colavo.android.ui.customerdetail.CustomerCreateFragment
import dagger.Subcomponent

@CustomerScope
@Subcomponent(modules = arrayOf(CustomerModule::class))
interface CustomerComponent {
    fun inject(placeholderFragment04: PlaceholderFragment04)  //TODO WTF
    fun inject(createCustomer: CustomerCreateFragment)  //TODO WTF
}