package com.colavo.android.presenters.checkout

import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.checkout.PaidType
import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.checkout.CreateCheckout
import com.colavo.android.interactors.checkout.GetSalonCheckout
import com.colavo.android.ui.checkout.CheckoutlistView
import com.colavo.android.utils.Logger
import rx.Subscriber
import javax.inject.Inject


class CheckoutPresenterImpl @Inject constructor(val getCheckout: GetSalonCheckout,
                                                val createCheckout: CreateCheckout) : CheckoutPresenter {

    var checkoutlistView: CheckoutlistView? = null
    lateinit var salonUid: String

    override fun attachView(checkoutlistView: CheckoutlistView) {
        this.checkoutlistView = checkoutlistView
    }

    override fun initialize(salonUid: String) {
        this.salonUid = salonUid
        Logger.log(salonUid)
        getCheckout.execute(salonUid, CheckoutSubscriber())
    }

    override fun onCreateCheckoutButtonClicked() {
        checkoutlistView?.showCreateCheckoutlistFragment()
    }

    override fun createCheckout(checkout_uid: String, salon_key: String, event_key: String, price: Double, is_manual_price: Boolean, reserve_fund: Double, paid_fund: Double, author_employee_key: String, paid_types: List<PaidType>, created_at: Double, updated_at: Double, reservedFund: Double, paidFund: Double, tip: Double) {
        createCheckout.execute(checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reservedFund
                ,paidFund, tip
                , CreateCheckoutSubscriber())
    }

    override fun onDestroy() {
        this.checkoutlistView = null
        //this.useCase.unsubscribe()
    }

    private inner class CreateCheckoutSubscriber : Subscriber<FirebaseResponse>() {

        override fun onNext(firebaseResponse: FirebaseResponse?) {
        }

        override fun onCompleted() {
        }

        override fun onError(throwable: Throwable?) {
            if(throwable != null) checkoutlistView?.onError(throwable)
        }

    }

    private inner class CheckoutSubscriber : Subscriber<Pair<CheckoutModel, ResponseType>>() {

        override fun onError(throwable: Throwable?) {
            Logger.log("response: Error")
            if(throwable != null) checkoutlistView?.onError(throwable)
        }

        override fun onCompleted() {}

        override fun onNext(pair: Pair<CheckoutModel, ResponseType>?) {
            when(pair?.second) {
                ResponseType.ADDED -> checkoutlistView?.addCheckout(pair?.first as CheckoutModel)
                ResponseType.CHANGED -> checkoutlistView?.changeCheckout(pair?.first as CheckoutModel)
                ResponseType.REMOVED -> checkoutlistView?.removeCheckout(pair?.first as CheckoutModel)
            }
        }

    }
}