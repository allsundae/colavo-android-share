package com.colavo.android.di.customer

import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.customer.CreateCustomer
import com.colavo.android.interactors.customer.GetSalonCustomer
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.repositories.customer.datasource.CustomerDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CustomerModule() {

    @Provides
    @CustomerScope
    fun getGetCustomer(customerRepository: CustomerRepository): GetSalonCustomer = GetSalonCustomer(customerRepository)

    @Provides
    @CustomerScope
    fun getCreateCustomer(customerRepository: CustomerRepository): CreateCustomer = CreateCustomer(customerRepository)

    @Provides
    @CustomerScope
    fun getCustomerRepository(customerDataSourceImpl: CustomerDataSourceImpl): CustomerRepository = CustomerRepository(customerDataSourceImpl)

    @Provides
    @CustomerScope
    fun getCustomerDataSourceImplementation(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase):
            CustomerDataSourceImpl = CustomerDataSourceImpl(retrofit, firebaseDatabase)

}