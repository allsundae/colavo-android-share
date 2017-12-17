package com.colavo.android.di.customer

import com.colavo.android.ui.PlaceholderFragment04
import dagger.Subcomponent

@CustomerScope
@Subcomponent(modules = arrayOf(CustomerModule::class))
interface CustomerComponent {
    fun inject(placeholderFragment04: PlaceholderFragment04)  //TODO WTF
}