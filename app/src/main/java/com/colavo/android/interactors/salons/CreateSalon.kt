package com.colavo.android.interactors.salons

import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.salons.SalonsRepository
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 01.08.2016.
 */
@UseCase
class CreateSalon @Inject constructor(salonsRepository: SalonsRepository) : SalonsUseCase(salonsRepository) {

    fun execute(salonName: String, salonAddress: String, ownerUid: String, subscriber: Subscriber<FirebaseResponse>)
            = super.execute(SalonsQuery.CreateSalon(salonName, salonAddress, ownerUid), subscriber)

}