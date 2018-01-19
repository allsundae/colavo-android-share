package com.colavo.android.ui.customerdetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerModel
import android.view.animation.AlphaAnimation
import com.colavo.android.ui.PlaceholderFragment04

import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.event.EventModel
import com.colavo.android.presenters.customerdetail.CustomerDetailPresenterImpl
import com.colavo.android.ui.PlaceholderFragment02
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.adapter.CustomerDetailAdapter
import com.colavo.android.utils.Logger
import com.colavo.android.utils.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_empty.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customerdetail.CustomerCreatePresenterImpl
import com.colavo.android.utils.showSnackBar
import kotlinx.android.synthetic.main.customer_create.*


class CustomerCreateFragment : BaseFragment(), CustomerCreateView {


    @Inject
    lateinit var customerCreatePresenter: CustomerCreatePresenterImpl

    var imageURL : ImageUrl = ImageUrl("","")


    override fun getLayout() = R.layout.customer_create


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as App).addCustomerComponent().inject(this)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle = arguments!!

        val salon = bundle.getSerializable(PlaceholderFragment04.EXTRA_SALON) as SalonModel
        customerCreatePresenter.attachView(createCustomerView = this)


//TODO        create_customer_image.setOnClickListener{customerCreatePresenter.createCustomerImage()}
        doneButton.setOnClickListener{customerCreatePresenter.createCustomer(salon.id, input_name.text.toString(), input_phone.text.toString(), imageURL )}

    }



    override fun onDestroy() {
        super.onDestroy()
        customerCreatePresenter.onDestroy()
        //clearCustomerDetailComponent()
    }
    override fun onError(throwable: Throwable) = (activity as AppCompatActivity).toast("${throwable.message}")

    override fun showToast(event: String) =  (activity as AppCompatActivity).toast(event)

    override fun showSnackbar(event: String) {
        create_customer.showSnackBar(event)
    }
    override fun showCreateProgress() {
        doneButton.visibility = View.GONE
        doneProgress.visibility = View.VISIBLE
    }

    override fun hideCreateProgress() {
        doneButton.visibility = View.VISIBLE
        doneProgress.visibility = View.GONE
    }

}


