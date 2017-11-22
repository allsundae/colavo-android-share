package com.colavo.android.di.checkout

import com.colavo.android.ui.PlaceholderFragment02
import dagger.Subcomponent

@CheckoutScope
@Subcomponent(modules = arrayOf(CheckoutModule::class))
interface CheckoutComponent {
    fun inject(placeholderFragment02: PlaceholderFragment02)  //TODO WTF
}