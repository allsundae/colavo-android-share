package com.colavo.android.presenters.customerdetail

import android.support.v4.content.res.TypedArrayUtils.getString
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.interactors.customer.CreateCustomer
import com.colavo.android.ui.customerdetail.CustomerCreateView
import rx.Subscriber
import javax.inject.Inject


class CustomerCreatePresenterImpl @Inject constructor(val createCustomer: CreateCustomer) : CustomerCreatePresenter {

    var createCustomerView: CustomerCreateView? = null
    lateinit var salonId: String

    override fun attachView(createCustomerView: CustomerCreateView) {
        this.createCustomerView = createCustomerView
    }

    override fun createCustomer(salonKey: String, name: String, phone: String, imageUrl: ImageUrl) {
        createCustomerView?.showCreateProgress()

        createCustomer.execute(salonKey, name, phone, imageUrl, CreateCustomerSubscriber())
    }

    override fun showCreateCustomerImage() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        this.createCustomerView = null
    }
    private inner class CreateCustomerSubscriber : Subscriber<FirebaseResponse>() {

        override fun onNext(firebaseResponse: FirebaseResponse?) {
           //TODO createCustomerView?.openSalonsActivity(user.uid)
        }

        override fun onError(e: Throwable) {
            createCustomerView?.hideCreateProgress()
            createCustomerView?.onError(e)
            createCustomerView?.onCreatedFailed()
        }

        override fun onCompleted() {
            createCustomerView?.hideCreateProgress()
            createCustomerView?.onCreatedSuccess()
        }
    }


    companion object {
        val COMPLETED_CREATE_CUSTOMER: String = "Customer creation has been completed"
    }
}