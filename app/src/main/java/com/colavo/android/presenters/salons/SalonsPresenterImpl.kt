package com.colavo.android.presenters.salons

import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.salons.CreateSalon
import com.colavo.android.interactors.salons.GetSalons
import com.colavo.android.ui.salons.SalonlistView
import com.colavo.android.utils.Logger
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 17.07.2016.
 */
class SalonsPresenterImpl @Inject constructor(val getSalons: GetSalons,
                                              val createSalon: CreateSalon) : SalonsPresenter {

    var salonlistView: SalonlistView? = null
    lateinit var ownerId: String

    override fun attachView(salonlistView: SalonlistView) {
        this.salonlistView = salonlistView
    }

    override fun initialize(ownerUid: String) {
        this.ownerId = ownerUid
        Logger.log(ownerUid)
        getSalons.execute(ownerUid, SalonsSubscriber())
    }

    override fun onCreateSalonButtonClicked() {
        salonlistView?.showCreateSalonlistFragment()
    }

    override fun createSalon(salonName: String, salonAddress: String, ownerUid: String) {
        createSalon.execute(salonName, salonAddress, ownerUid, CreateSalonSubscriber())
    }

    override fun onDestroy() {
        this.salonlistView = null
        //this.useCase.unsubscribe()
    }

    private inner class CreateSalonSubscriber : Subscriber<FirebaseResponse>() {

        override fun onNext(firebaseResponse: FirebaseResponse?) {
        }

        override fun onCompleted() {
        }

        override fun onError(throwable: Throwable?) {
            if(throwable != null) salonlistView?.onError(throwable)
        }

    }

    private inner class SalonsSubscriber : Subscriber<Pair<SalonModel, ResponseType>>() {

        override fun onError(throwable: Throwable?) {
            Logger.log("response: Error")
            if(throwable != null) salonlistView?.onError(throwable)
        }

        override fun onCompleted() {}

        override fun onNext(pair: Pair<SalonModel, ResponseType>?) {
            when(pair?.second) {
                ResponseType.ADDED -> salonlistView?.addSalon(pair?.first as SalonModel)
                ResponseType.CHANGED -> salonlistView?.changeSalon(pair?.first as SalonModel)
                ResponseType.REMOVED -> salonlistView?.removeSalon(pair?.first as SalonModel)
            }
        }

    }
}