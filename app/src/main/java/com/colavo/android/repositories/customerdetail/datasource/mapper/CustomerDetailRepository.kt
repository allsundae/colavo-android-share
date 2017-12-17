package com.colavo.android.repositories.customerdetail

import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.checkout.datasource.CheckoutDataSourceImpl
import com.colavo.android.repositories.customerdetail.datasource.CustomerDetailDataSourceImpl
import com.colavo.android.utils.HandleUtils
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerDetailRepository @Inject constructor(customerDetailDataSourceImpl: CustomerDetailDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, customerDetailDataSourceImpl)
    }

    fun <T> query(customerDetailQuery: CustomerDetailQuery): Observable<T> = getObservable(customerDetailQuery)

}