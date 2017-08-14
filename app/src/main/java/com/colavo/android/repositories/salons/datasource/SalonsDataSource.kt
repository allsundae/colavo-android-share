package com.colavo.android.repositories.salons.datasource

import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.query.Handle
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import rx.Observable

/**
 * Created by RUS on 17.07.2016.
 */
interface SalonsDataSource {

    @Handle(SalonsQuery.GetSalons::class)
    fun initialize(query: SalonsQuery.GetSalons): Observable<Pair<SalonModel, ResponseType>>

    @Handle(SalonsQuery.CreateSalon::class)
    fun createSalon(query: SalonsQuery.CreateSalon): Observable<FirebaseResponse>

}