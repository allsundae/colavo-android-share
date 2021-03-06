package com.colavo.android.di.app

import com.colavo.android.di.checkout.CheckoutComponent
import com.colavo.android.di.checkout.CheckoutModule
import com.colavo.android.di.customer.CustomerComponent
import com.colavo.android.di.customer.CustomerModule
import com.colavo.android.di.customerdetail.CustomerDetailComponent
import com.colavo.android.di.customerdetail.CustomerDetailModule
import com.colavo.android.di.event.EventComponent
import com.colavo.android.di.event.EventModule
import com.colavo.android.di.salons.SalonsComponent
import com.colavo.android.di.salons.SalonsModule
import com.colavo.android.di.firebase.FirebaseModule
import com.colavo.android.di.net.NetModule
import com.colavo.android.di.session.SessionComponent
import com.colavo.android.di.session.SessionModule
import com.colavo.android.ui.MainActivity
import com.colavo.android.ui.PlaceholderFragment
import com.colavo.android.ui.PlaceholderFragment04
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, FirebaseModule::class, NetModule::class))
@Singleton
interface AppComponent {
    fun addEventComponent(eventModule: EventModule): EventComponent
    fun addSessionComponent(sessionModule: SessionModule): SessionComponent
    fun addSalonsComponent(salonModule: SalonsModule): SalonsComponent
    fun addCustomerComponent(customerModule: CustomerModule): CustomerComponent
    fun addCheckoutComponent(checkoutModule: CheckoutModule): CheckoutComponent
    fun addCustomerDetailComponent(customerDetailModule: CustomerDetailModule): CustomerDetailComponent

    fun inject(mainActivity: MainActivity)
  //  fun inject(placeholderFragment04: PlaceholderFragment04)
}