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


class SalonsPresenterImpl @Inject constructor(val getSalons: GetSalons,
                                              val createSalon: CreateSalon) : SalonsPresenter {

    var salonlistView: SalonlistView? = null
    lateinit var ownerId: String

    override fun attachView(salonlistView: SalonlistView) {
        this.salonlistView = salonlistView
    }

    override fun initialize(ownerUid: String) {
        this.ownerId = ownerUid
        Logger.log("SalonsPresenterImpl : initialize : ${ownerUid}")
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
            Logger.log("SalonsSubscriber : response: Error")
            salonlistView?.hideProgress()
            if(throwable != null) salonlistView?.onError(throwable)
        }

        override fun onCompleted() {
            salonlistView?.hideProgress()
        }

        override fun onNext(pair: Pair<SalonModel, ResponseType>?) {
            salonlistView?.hideProgress()
            when(pair?.second) {
                ResponseType.ADDED -> salonlistView?.addSalon(pair?.first as SalonModel)
                ResponseType.CHANGED -> salonlistView?.changeSalon(pair?.first as SalonModel)
                ResponseType.REMOVED -> salonlistView?.removeSalon(pair?.first as SalonModel)
            }
        }

    }
}