package com.colavo.android.presenters.customer

import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.customer.CreateCustomer
import com.colavo.android.interactors.customer.GetSalonCustomer
import com.colavo.android.interactors.salons.CreateSalon
import com.colavo.android.interactors.salons.GetSalons
import com.colavo.android.ui.customer.CustomerlistView
import com.colavo.android.ui.salons.SalonlistView
import com.colavo.android.utils.Logger
import rx.Subscriber
import javax.inject.Inject


class CustomerPresenterImpl @Inject constructor(val getCustomer: GetSalonCustomer,
                                                val createCustomer: CreateCustomer) : CustomerPresenter {

    var customerlistView: CustomerlistView? = null
    lateinit var salonUid: String

    override fun attachView(customerlistView: CustomerlistView) {
        this.customerlistView = customerlistView
    }

    override fun initialize(salonUid: String) {
        this.salonUid = salonUid
        Logger.log(salonUid)
        getCustomer.execute(salonUid, CustomerSubscriber())
    }

    override fun onCreateCustomerButtonClicked() {
        customerlistView?.showCreateCustomerlistFragment()
    }

    override fun createCustomer
            (customerUid: String
             , customerPhone: String
             , customerName: String
             , customerImageUrls:List<ImageUrl>) {
        createCustomer.execute(
                salonUid, customerUid, customerPhone, customerName, customerImageUrls, CreateCustomerSubscriber())
    }

    override fun onDestroy() {
        this.customerlistView = null
        //this.useCase.unsubscribe()
    }

    private inner class CreateCustomerSubscriber : Subscriber<FirebaseResponse>() {

        override fun onNext(firebaseResponse: FirebaseResponse?) {
        }

        override fun onCompleted() {

        }

        override fun onError(throwable: Throwable?) {
            if(throwable != null) customerlistView?.onError(throwable)
        }

    }

    private inner class CustomerSubscriber : Subscriber<Pair<CustomerModel, ResponseType>>() {

        override fun onError(throwable: Throwable?) {
            Logger.log("CustomerSubscriber : response: Error")
            if(throwable != null) customerlistView?.onError(throwable)
        }

        override fun onCompleted() {
            customerlistView?.updateNumberofCustomer()
            Logger.log("CustomerSubscriber: COMPLETED")
        }

        override fun onNext(pair: Pair<CustomerModel, ResponseType>?) {
            when(pair?.second) {
                ResponseType.ADDED -> customerlistView?.addCustomer(pair?.first as CustomerModel)
                ResponseType.CHANGED -> customerlistView?.changeCustomer(pair?.first as CustomerModel)
                ResponseType.REMOVED -> customerlistView?.removeCustomer(pair?.first as CustomerModel)
            }
        }

    }
}