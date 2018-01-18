package com.colavo.android.di.customer

import com.colavo.android.di.customerdetail.CustomerDetailModule
import com.colavo.android.di.customerdetail.CustomerDetailScope
import com.colavo.android.ui.customerdetail.CustomerCreateFragment
import com.colavo.android.ui.customerdetail.CustomerDetailFragment
import dagger.Subcomponent

@CustomerCreateScope
@Subcomponent(modules = arrayOf(CustomerCreateModule::class))
interface CustomerCreatelComponent {
    fun inject(customerCreateFragment: CustomerCreateFragment)
}