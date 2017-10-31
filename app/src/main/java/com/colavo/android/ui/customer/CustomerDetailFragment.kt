package com.colavo.android.ui.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customer.CustomerPresenterImpl
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CustomerDetailFragment : BaseFragment(), CustomerlistView
        , CustomerAdapter.OnItemClickListener {

    @Inject
    lateinit var customerPresenter: CustomerPresenterImpl
    lateinit var customerAdapter: CustomerAdapter

    override fun getLayout() = R.layout.fragment_04

    companion object {
        fun newInstance() = CustomerDetailFragment()
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

        //(context.applicationContext as App).addCustomerComponent().inject(this)

        //   (context.applicationContext as App).addCustomerComponent().inject(this)

    //    (getActivity().getApplication() as App).addCustomerComponent().inject(this)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle (R.string.bottom_navi_4)
        toolBar?.inflateMenu(R.menu.menu_customer)

        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel

      //  (application as App).addCustomerComponent().inject(this)
        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        fab_customer.setOnClickListener { customerPresenter.onCreateCustomerButtonClicked()}

        customerPresenter.attachView(this)
        customerPresenter.initialize(salon.id)*/

    }




    override fun setCustomerlist(customerEntities: List<CustomerModel>?) {
        //customers_recyclerView.adapter = CustomerAdapter(this, customerEntities!!.toMutableList()) //todo
    }

    override fun showCreateCustomerlistFragment() {
        //todo
/*        MaterialDialog.Builder(this.context)
                .title(R.string.create_conversation)
                .content(R.string.input_salon_name)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText(R.string.create_conversation)
                .input("", "", false) { dialog, input -> customerPresenter?.createCustomer(
                        "customerUid"
                        , "010-4707-9934"
                        , input.toString()
                        , "image"
                )}.show()*/
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

/*
        val position = (customers_recyclerView.adapter as CustomerAdapter).items.indexOfFirst { it.uid.equals(customerEntity.uid) }
        (customers_recyclerView.adapter as CustomerAdapter).items[position] = customerEntity
        customers_recyclerView.adapter.notifyItemChanged(position)
*/

/*
        val position = customerAdapter.items.indexOfFirst { it.uid.equals(customerEntity.uid) }
        customerAdapter.items[position] = customerEntity
        customerAdapter.notifyItemChanged(position)
*/
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
/*        val intent = Intent(this, CustomerDetailFragment::class.java)
        intent.putExtra(EXTRA_CUSTOMER, item)
        startActivity(intent)*/
    }


    override fun onLongItemClicked(item: CustomerModel) {
    }

    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        showToast(event)
    }

    override fun showSnackbar(event: String) {
//        customers_recyclerView.showSnackBar(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        //clearCustomerComponent()
        customerPresenter.onDestroy()
    }

}