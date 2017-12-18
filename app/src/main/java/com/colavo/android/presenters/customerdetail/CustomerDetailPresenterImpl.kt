package com.colavo.android.presenters.customerdetail

import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.customerdetail.GetCustomerEvents
import com.colavo.android.ui.customerdetail.CustomerDetailListView
import com.colavo.android.utils.Logger
import rx.Subscriber
import javax.inject.Inject


class CustomerDetailPresenterImpl @Inject constructor(val getCustomerEvents: GetCustomerEvents)
                                : CustomerDetailPresenter {

    var customerDetailListView: CustomerDetailListView? = null
    lateinit var customerUid: String

    override fun attachView(customerDetailListView: CustomerDetailListView) {
        this.customerDetailListView = customerDetailListView
    }

    override fun initialize(customerUid: String) {
        this.customerUid = customerUid
        Logger.log("CustomerDetailPresenterImpl : initialize : ${customerUid}")
        getCustomerEvents.execute(customerUid, CustomerDetailSubscriber())
    }


    override fun onDestroy() {
        this.customerDetailListView = null
        //this.useCase.unsubscribe()
    }

    private inner class CreateCheckoutSubscriber : Subscriber<FirebaseResponse>() {

        override fun onNext(firebaseResponse: FirebaseResponse?) {
        }

        override fun onCompleted() {
        }

        override fun onError(throwable: Throwable?) {
            if(throwable != null) customerDetailListView?.onError(throwable)
        }

    }

    private inner class CustomerDetailSubscriber : Subscriber<Pair<CustomerDetailModel, ResponseType>>() {

        override fun onError(throwable: Throwable?) {
            Logger.log("CustomerDetailSubscriber : response: Error")
            if(throwable != null) customerDetailListView?.onError(throwable)
        }

        override fun onCompleted() {
            Logger.log("CustomerDetailSubscriber : onCompleted ( )")
        }

        override fun onNext(pair: Pair<CustomerDetailModel, ResponseType>?) {
            Logger.log("CustomerDetailSubscriber : onNext(customer_uid) : ${pair?.first?.id}")
            when(pair?.second) {
                ResponseType.ADDED -> customerDetailListView?.addCustomerDetail(pair?.first as CustomerDetailModel)
                ResponseType.CHANGED -> customerDetailListView?.changeCustomerDetail(pair?.first as CustomerDetailModel)
                ResponseType.REMOVED -> customerDetailListView?.removeCustomerDetail(pair?.first as CustomerDetailModel)
            }
        }

    }
}