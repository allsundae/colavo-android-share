package com.colavo.android.di.checkout

import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.checkout.CreateCheckout
import com.colavo.android.interactors.checkout.GetSalonCheckout
import com.colavo.android.repositories.checkout.CheckoutRepository
import com.colavo.android.repositories.checkout.datasource.CheckoutDataSourceImpl
import com.colavo.android.utils.SimpleCallback
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CheckoutModule {

    @Provides
    @CheckoutScope
    fun getGetCheckout(checkoutRepository: CheckoutRepository): GetSalonCheckout = GetSalonCheckout(checkoutRepository)

    @Provides
    @CheckoutScope
    fun getCreateCheckout(checkoutRepository: CheckoutRepository): CreateCheckout = CreateCheckout(checkoutRepository)

    @Provides
    @CheckoutScope
    fun getCheckoutRepository(checkoutDataSourceImpl: CheckoutDataSourceImpl): CheckoutRepository = CheckoutRepository(checkoutDataSourceImpl)

    @Provides
    @CheckoutScope
    fun getCheckoutDataSourceImplementation(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase):
            CheckoutDataSourceImpl = CheckoutDataSourceImpl(retrofit, firebaseDatabase)

}