package com.colavo.android.di.customerdetail

import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.customerdetail.GetCustomerEvents
import com.colavo.android.repositories.customerdetail.CustomerDetailRepository
import com.colavo.android.repositories.customerdetail.datasource.CustomerDetailDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CustomerDetailModule {

    @Provides
    @CustomerDetailScope
    fun getGetCustomerDetail(customerDetailRepository: CustomerDetailRepository): GetCustomerEvents = GetCustomerEvents(customerDetailRepository)

  /*  @Provides
    @CustomerDetailScope
    fun getCreateCustomerDetail(customerDetailRepository: CustomerDetailRepository): CreateCustomerDetail = CreateCustomerDetail(customerDetailRepository)
*/
    @Provides
    @CustomerDetailScope
    fun getCustomerDetailRepository(customerDetailDataSourceImpl: CustomerDetailDataSourceImpl): CustomerDetailRepository = CustomerDetailRepository(customerDetailDataSourceImpl)

    @Provides
    @CustomerDetailScope
    fun getCustomerDetailDataSourceImplementation(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase):
            CustomerDetailDataSourceImpl = CustomerDetailDataSourceImpl(retrofit, firebaseDatabase)

}