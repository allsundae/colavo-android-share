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
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.view.View.OnTouchListener
import ooo.oxo.library.widget.PullBackLayout



class CustomerCreateFragment : BaseFragment(), CustomerCreateView, PullBackLayout.Callback {


    @Inject
    lateinit var customerCreatePresenter: CustomerCreatePresenterImpl

    var imageURL : ImageUrl = ImageUrl("","")


    override fun getLayout() = R.layout.customer_create


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as App).addCustomerComponent().inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle = arguments!!

        val salon = bundle.getSerializable(PlaceholderFragment04.EXTRA_SALON) as SalonModel
        customerCreatePresenter.attachView(createCustomerView = this)
        puller.setCallback(this)

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

   /* fun onTouchEvent(event: MotionEvent): Boolean {
        // I only care if the event is an UP action
        if (event.action == MotionEvent.ACTION_UP) {
            //and only is the ListFragment shown.
            if (isListFragmentShown) {
                // create a rect for storing the fragment window rect
                val r = Rect(0, 0, 0, 0)
                // retrieve the fragment's windows rect
                currentFragment.getView().getHitRect(r)
                // check if the event position is inside the window rect
                val intersects = r.contains(event.x.toInt(), event.y.toInt())
                // if the event is not inside then we can close the fragment
                if (!intersects) {
                    Log.d(TAG, "pressed outside the listFragment")
                    val fragmentTransaction: FragmentTransaction
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(currentFragment).commit()
                    // notify that we consumed this event
                    return true
                }
            }
        }
        //let the system handle the event
        return super.onTouchEvent(event)
    }*/

    override fun onPullStart() {
        // fade out Action Bar ...
        // show Status Bar ...
    }

    override fun onPull(progress: Float) {
        // set the opacity of the window's background
    }

    override fun onPullCancel() {
        // fade in Action Bar
    }

    override fun onPullComplete() {
        //supportFinishAfterTransition()
    }

}


