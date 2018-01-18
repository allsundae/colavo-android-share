package com.colavo.android.di.customer

import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.customerdetail.GetCustomerEvents
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.repositories.customer.datasource.CustomerDataSourceImpl
import com.colavo.android.repositories.customerdetail.CustomerDetailRepository
import com.colavo.android.repositories.customerdetail.datasource.CustomerDetailDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CustomerCreateModule {

    @Provides
    @CustomerCreateScope
    fun getCustomerCreateRepository(customerDataSourceImpl: CustomerDataSourceImpl): CustomerRepository = CustomerRepository(customerDataSourceImpl)

    @Provides
    @CustomerCreateScope
    fun getCustomerCreateDataSourceImplementation(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase):
            CustomerDataSourceImpl = CustomerDataSourceImpl(retrofit, firebaseDatabase)

}