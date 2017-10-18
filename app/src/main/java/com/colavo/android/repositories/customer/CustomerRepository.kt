package com.colavo.android.repositories.customer

import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.customer.datasource.CustomerDataSourceImpl
import com.colavo.android.repositories.salons.datasource.SalonsDataSourceImpl
import com.colavo.android.utils.HandleUtils
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(customerDataSourceImpl: CustomerDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, customerDataSourceImpl)
    }

    fun <T> query(customerQuery: CustomerQuery): Observable<T> = getObservable(customerQuery)

}