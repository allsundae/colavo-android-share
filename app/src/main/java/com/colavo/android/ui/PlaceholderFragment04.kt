package com.colavo.android.ui

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.base.BaseFragment
import com.colavo.android.base.BasePresenterFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.main.presenter.MainContract
import com.colavo.android.presenters.customer.CustomerPresenter
import com.colavo.android.presenters.customer.CustomerPresenterImpl
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.customer.CustomerlistView
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.colavo.android.utils.showSnackBar
import com.colavo.android.utils.toast
import com.colavo.android.view.main.presenter.MainPresenter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_04.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject



class PlaceholderFragment04 : BaseFragment(), CustomerlistView
        , CustomerAdapter.OnItemClickListener {
    override fun onError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(event: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSnackbar(event: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCustomerlist(customerEntities: List<CustomerModel>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCreateCustomerlistFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openCustomerFragment(customerModel: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCustomer(customerEntity: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeCustomer(customerEntity: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCustomer(customerEntity: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(item: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongItemClicked(item: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    //lateinit var customerPresenter: CustomerPresenterImpl
    var customerPresenter: CustomerPresenterImpl? = null
    lateinit var customerAdapter: CustomerAdapter

    override fun getLayout() = R.layout.fragment_04

    companion object {
        fun newInstance() = PlaceholderFragment04()
    }

    private val progressDialog: MaterialDialog by lazy {
        MaterialDialog.Builder(this.context)
                .title(R.string.customers_loading)
                .content(R.string.wait)
                .progress(true, 0)
                .build()
    }
    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // (activity.application as App).getNetComponent().inject(this)
        //       (getActivity().getApplication() as App).addCustomerComponent().inject(this)
        (getActivity().getApplication() as App).addCustomerComponent().inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        (activity as AppCompatActivity).supportActionBar?.setTitle (R.string.bottom_navi_4)
        toolBar.inflateMenu(menu_customer)

/*        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter
        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        customerPresenter?.attachView(this)
        customerPresenter?.initialize(salon.id)*/

        (activity.application as App).addCustomerComponent().inject(this)
        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter
        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel

        customerPresenter?.attachView(this)
        customerPresenter?.initialize(salon.id)
    }



    override fun onDestroy() {
        super.onDestroy()
        //clearCustomerComponent()
    }



}
