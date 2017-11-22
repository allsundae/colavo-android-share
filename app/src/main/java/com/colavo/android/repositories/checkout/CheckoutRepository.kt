package com.colavo.android.repositories.checkout

import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.checkout.datasource.CheckoutDataSourceImpl
import com.colavo.android.utils.HandleUtils
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutRepository @Inject constructor(checkoutDataSourceImpl: CheckoutDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, checkoutDataSourceImpl)
    }

    fun <T> query(checkoutQuery: CheckoutQuery): Observable<T> = getObservable(checkoutQuery)

}