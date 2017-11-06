package com.colavo.android.repositories.customer.datasource

import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.query.Handle
import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import rx.Observable

/**
 * Created by RUS on 17.07.2016.
 */
interface CustomerDataSource {

    @Handle(CustomerQuery.GetCustomer::class)
    fun initialize(query: CustomerQuery.GetCustomer): Observable<Pair<CustomerModel, ResponseType>>

    @Handle(CustomerQuery.CreateCustomer::class)
    fun createCustomer(query: CustomerQuery.CreateCustomer): Observable<FirebaseResponse>

}