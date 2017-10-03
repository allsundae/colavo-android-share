package com.colavo.android.repositories.salons

import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.salons.datasource.SalonsDataSourceImpl
import com.colavo.android.utils.HandleUtils
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by RUS on 17.07.2016.
 */
@Singleton
class SalonsRepository @Inject constructor(salonsDataSourceImpl: SalonsDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, salonsDataSourceImpl)
    }

    fun <T> query(salonsQuery: SalonsQuery): Observable<T> = getObservable(salonsQuery)

}