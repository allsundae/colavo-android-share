package com.colavo.android.interactors.salons

import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.salons.SalonsRepository
import rx.Subscriber
import javax.inject.Inject

@UseCase
class GetSalons @Inject constructor(salonsRepository: SalonsRepository) : SalonsUseCase(salonsRepository) {

    fun execute(ownerUid:String, subscriber: Subscriber<Pair<SalonModel, ResponseType>>)
            = super.execute(SalonsQuery.GetSalons(ownerUid), subscriber)

}