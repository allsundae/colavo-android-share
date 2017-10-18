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



class PlaceholderFragment04 : BaseFragment()
        , CustomerlistView, CustomerAdapter.OnItemClickListener {


    @Inject
    lateinit var customerPresenter: CustomerPresenterImpl
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
                .build() }
    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // (activity.application as App).getNetComponent().inject(this)
//        (getActivity().getApplication() as App).addCustomerComponent().inject(this)
//TODO WTF        (getActivity().getApplication() as App).addCustomerComponent().inject(this)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)

     //TODO WTF   (activity as App).addCustomerComponent().inject(this)
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        (activity as AppCompatActivity).supportActionBar?.setTitle (salon.name)

        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter
        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        customerPresenter!!.attachView(this)
        customerPresenter!!.initialize(salon.id)

    }




    override fun setCustomerlist(customerEntities: List<CustomerModel>?) {
        customers_recyclerView.adapter = CustomerAdapter(this, customerEntities!!.toMutableList()) //todo
    }

    override fun showCreateCustomerlistFragment() {
         //todo
        MaterialDialog.Builder(this.context)
                .title(R.string.create_conversation)
                .content(R.string.input_salon_name)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText(R.string.create_conversation)
                .input("", "", false) { dialog, input -> customerPresenter?.createCustomer(
                        "customerUid"
                        , "010-4707-9934"
                        , input.toString()
                        , "image_url"
                )}.show()
    }

    override fun openCustomerFragment(customerModel: CustomerModel) {
         //todo
/*
        val intent = Intent(this, SalonMainActivity::class.java)
        intent.putExtra(SalonListActivity.EXTRA_CONVERSATION, salonModel)
        startActivity(intent)

        */
    }


    override fun addCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer added")

        customerAdapter.items.add(customerEntity)
        customerAdapter.notifyItemInserted(customerAdapter.itemCount)
    }

    override fun changeCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer changed")

        val position = customerAdapter.items.indexOfFirst { it.uid.equals(customerEntity.uid) }
        customerAdapter.items[position] = customerEntity
        customerAdapter.notifyItemChanged(position)
    }

    override fun removeCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer removed")

        val position = customerAdapter.items.indexOfFirst { it.uid.equals(customerEntity) }
        customerAdapter.items.removeAt(position)
        customerAdapter.notifyItemRemoved(position)
    }


    override fun showProgress() {
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.hide()
    }

    override fun onItemClicked(item: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onLongItemClicked(item: CustomerModel) {
    }

    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        (activity as AppCompatActivity).toast(event)
    }

    override fun showSnackbar(event: String) {
        customers_recyclerView.showSnackBar(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        //clearCustomerComponent()
        customerPresenter?.onDestroy()
    }
}
