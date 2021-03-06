package com.colavo.android

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.colavo.android.di.app.AppComponent
import com.colavo.android.di.app.AppModule
import com.colavo.android.di.app.DaggerAppComponent
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
import com.colavo.android.repositories.salons.SalonsRepository
import com.colavo.android.repositories.salons.datasource.SalonsDataSourceImpl
import com.colavo.android.repositories.session.SessionRepository
import com.colavo.android.repositories.session.datasource.SessionDataSourceImpl
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.repositories.customer.datasource.CustomerDataSourceImpl
import com.colavo.android.utils.HandleUtils
import com.colavo.android.utils.Logger
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.tsengvn.typekit.Typekit


class App : Application() {

    lateinit var appComponent: AppComponent
    private var sessionComponent: SessionComponent? = null
    private var salonsComponent: SalonsComponent? = null
    private var eventComponent: EventComponent? = null
    private var customerComponent: CustomerComponent? = null
    private var checkoutComponent: CheckoutComponent? = null
    private var customerDetailComponent: CustomerDetailComponent? = null

    override fun onCreate() {
        super.onCreate()

/*        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            Logger.log("FirebaseDatabase setPersistenceEnabled : TRUE")
        }*/

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this,"NanumSquareR.ttf"))
                .addBold(Typekit.createFromAsset(this,"Poppins-SemiBold.ttf"))
                .addCustom1(Typekit.createFromAsset(this,"Poppins-Light.ttf"))
/*
                .addNormal(Typekit.createFromAsset(this,"Poppins-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this,"Poppins-Bold.ttf"))
*/
        createAppComponent()
    }

    fun createAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun addSessionComponent(): SessionComponent {
        if(sessionComponent == null) {
            sessionComponent = appComponent.addSessionComponent(SessionModule())
        }
        return sessionComponent as SessionComponent
    }

    fun addSalonsComponent(): SalonsComponent {
        if(salonsComponent == null) {
            salonsComponent = appComponent.addSalonsComponent(SalonsModule())
        }
        return salonsComponent as SalonsComponent
    }

    fun addEventComponent(): EventComponent {
        if(eventComponent == null) {
            eventComponent = appComponent.addEventComponent(EventModule())
        }
        return eventComponent as EventComponent
    }

    fun addCustomerComponent(): CustomerComponent {
        if(customerComponent == null) {
            customerComponent = appComponent.addCustomerComponent(CustomerModule())
        }
        return customerComponent as CustomerComponent
    }

    fun addCheckoutComponent(): CheckoutComponent {
        if(checkoutComponent == null) {
            checkoutComponent = appComponent.addCheckoutComponent(CheckoutModule())
        }
        return checkoutComponent as CheckoutComponent
    }

    fun addCustomerDetailComponent(): CustomerDetailComponent {
        if(customerDetailComponent == null) {
            customerDetailComponent = appComponent.addCustomerDetailComponent(CustomerDetailModule())
        }
        return customerDetailComponent as CustomerDetailComponent
    }

    fun clearSessionComponent() {
        sessionComponent = null
    }

    fun clearSalonsComponent() {
        salonsComponent = null
    }

    fun clearEventComponent() {
        eventComponent = null
    }

    fun clearCustomerComponent() {
        customerComponent = null
    }

}