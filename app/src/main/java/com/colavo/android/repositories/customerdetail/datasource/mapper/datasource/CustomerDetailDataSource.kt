package com.colavo.android.repositories.customerdetail.datasource

import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.query.Handle
import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.entity.response.ResponseType
import rx.Observable


interface CustomerDetailDataSource {

    @Handle(CustomerDetailQuery.GetCustomerEvents::class)
    fun initialize(query: CustomerDetailQuery.GetCustomerEvents): Observable<Pair<CustomerDetailModel, ResponseType>>

    /*@Handle(CustomerDetailQuery.CreateCustomerDetail::class)
    fun createCustomerDetail(query: CustomerDetailQuery.CreateCustomerDetail): Observable<FirebaseResponse>
*/
}