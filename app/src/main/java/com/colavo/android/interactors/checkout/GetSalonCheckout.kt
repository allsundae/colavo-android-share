package com.colavo.android.interactors.checkout

import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.checkout.CheckoutRepository
import rx.Subscriber
import javax.inject.Inject


@UseCase
class GetSalonCheckout @Inject constructor(checkoutRepository: CheckoutRepository) : CheckoutUseCase(checkoutRepository) {

    fun execute(salonUid:String, subscriber: Subscriber<Pair<CheckoutModel, ResponseType>>)
            = super.execute(CheckoutQuery.GetCheckout(salonUid), subscriber)

}