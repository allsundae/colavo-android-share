package com.colavo.android.ui.customerdetail

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.ui.PlaceholderFragment04
import android.view.*
import com.colavo.android.App
import com.colavo.android.base.BaseFragment
import com.colavo.android.utils.toast
import javax.inject.Inject
import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customerdetail.CustomerCreatePresenterImpl
import com.colavo.android.utils.showSnackBar
import kotlinx.android.synthetic.main.customer_create.*
import android.support.design.widget.BottomSheetBehavior
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


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
        doneButton.setOnClickListener{ checkField(salon) }

        touch_outside.setOnClickListener({ v -> dismissFragment() }) //(activity as AppCompatActivity).finish() })
        BottomSheetBehavior.from(bottom_sheet)
                .setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> dismissFragment()//(activity as AppCompatActivity).finish()
                          //  BottomSheetBehavior.STATE_EXPANDED ->   setStatusBarDim(false)
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        // no op
                    }
                })
    }

    private fun dismissFragment() {
        //if Keyboard is visible, hide it
        val view = (activity as AppCompatActivity).getCurrentFocus()
        if (view != null) {
            val imm = (activity as AppCompatActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }

        // back to parent view
        getFragmentManager()?.popBackStackImmediate()
    }

    private fun checkField(salon: SalonModel) {
        var myInternationalNumber: String =""

        if (input_name.text.toString()=="") {
            showSnackbar (getString(R.string.err_name))
        }
        else{
            if (input_phone.isValid()) {
                myInternationalNumber = input_phone.getNumber()
                customerCreatePresenter.createCustomer(salon.id, input_name.text.toString(), myInternationalNumber, imageURL) //input_phone.text.toString()
            }
            else
            {
                showSnackbar (getString(R.string.err_phone_notvalid))
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        customerCreatePresenter.onDestroy()
        //clearCustomerDetailComponent()
    }
    override fun onError(throwable: Throwable) = (activity as AppCompatActivity).toast("${throwable.message}")

    override fun onCreatedSuccess() {
        showToast (getString(R.string.success_create_customer))
        dismissFragment()
    }
    override fun onCreatedFailed() {
        showToast (getString(R.string.err_create_customer))
        dismissFragment()
    }

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


