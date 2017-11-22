package com.colavo.android.repositories.checkout.datasource

import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.query.Handle
import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import rx.Observable


interface CheckoutDataSource {

    @Handle(CheckoutQuery.GetCheckout::class)
    fun initialize(query: CheckoutQuery.GetCheckout): Observable<Pair<CheckoutModel, ResponseType>>

    @Handle(CheckoutQuery.CreateCheckout::class)
    fun createCheckout(query: CheckoutQuery.CreateCheckout): Observable<FirebaseResponse>

}