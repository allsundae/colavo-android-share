package com.colavo.android.di.customerdetail

import com.colavo.android.ui.customerdetail.CustomerDetailFragment
import dagger.Subcomponent

@CustomerDetailScope
@Subcomponent(modules = arrayOf(CustomerDetailModule::class))
interface CustomerDetailComponent {
    fun inject(customerDetailFragment: CustomerDetailFragment)
}