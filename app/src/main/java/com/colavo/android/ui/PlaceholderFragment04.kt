package com.colavo.android.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customer.CustomerPresenterImpl
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.customer.CustomerDetailFragment
import com.colavo.android.ui.customer.CustomerlistView
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_04.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject



class PlaceholderFragment04 : BaseFragment(), CustomerlistView
        , CustomerAdapter.OnItemClickListener {

    @Inject
    lateinit var customerPresenter: CustomerPresenterImpl
    lateinit var customerAdapter: CustomerAdapter

    override fun getLayout() = R.layout.fragment_04

    companion object {
        fun newInstance() = PlaceholderFragment04()
        val EXTRA_CUSTOMER: String = "CUSTOMER"
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

        (context.applicationContext as App).addCustomerComponent().inject(this)

        //   (context.applicationContext as App).addCustomerComponent().inject(this)

    //    (getActivity().getApplication() as App).addCustomerComponent().inject(this)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel

      //  (application as App).addCustomerComponent().inject(this)
        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        fab_customer.setOnClickListener { customerPresenter.onCreateCustomerButtonClicked()}

        customerPresenter.attachView(this)
        customerPresenter.initialize(salon.id)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle ("Customers ${customerAdapter.getItemCount()}" ) //R.string.bottom_navi_4
        toolBar?.inflateMenu(menu_customer)
        //TODO update number of customers
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
                        , Map({ 'name':'Future Studio Steak House', 'name':'Future Studio Steak House' })
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
        Logger.log("Customer added :  " + customerEntity.name)

        customerAdapter.items.add(customerEntity)
        customerAdapter.notifyItemInserted(customerAdapter.itemCount)
    }

    override fun changeCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer changed")

        val position = (customers_recyclerView.adapter as CustomerAdapter).items.indexOfFirst { it.uid.equals(customerEntity.uid) }
        (customers_recyclerView.adapter as CustomerAdapter).items[position] = customerEntity
        customers_recyclerView.adapter.notifyItemChanged(position)

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
/*
        val intent = Intent(this.context, CustomerDetailFragment::class.java)
        intent.putExtra(EXTRA_CUSTOMER, item)
        startActivity(intent)
*/
/*
        val dialogFrag = CreateFabFragment.newInstance()
        dialogFrag.setParentFab(fab_customer as FloatingActionButton)
        dialogFrag.show((activity as AppCompatActivity).getSupportFragmentManager(), dialogFrag.getTag())
*/

// Create new fragment and transaction
        val newFragment = CustomerDetailFragment()
        val transaction = fragmentManager.beginTransaction()

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.customer_list_holder, newFragment) //container
        transaction.addToBackStack(null)

// Commit the transaction
        transaction.commit()


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
