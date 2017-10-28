package com.colavo.android.ui.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customer.CustomerPresenter
import com.colavo.android.presenters.customer.CustomerPresenterImpl
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.colavo.android.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_04.*
import javax.inject.Inject

class CustomerListActivity : AppCompatActivity()
        , CustomerlistView, CustomerAdapter.OnItemClickListener {

    @Inject
    lateinit var customerPresenter: CustomerPresenterImpl
    lateinit var customerAdapter: CustomerAdapter
    private val progressDialog: MaterialDialog by lazy {
        MaterialDialog.Builder(this)
                .title(R.string.customers_loading)
                .content(R.string.wait)
                .progress(true, 0)
                .build() }
    @Inject
    lateinit var firebaseAuth: FirebaseAuth


/*
    companion object {
        fun newInstance() = CustomerListActivity()
    }
*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_04)

//        setSupportActionBar(toolBar)

       (application as App).addCustomerComponent().inject(this)
        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter

        val salon = intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        supportActionBar?.setTitle (salon.name)


        customers_recyclerView.layoutManager = LinearLayoutManager(this)

        fab_customer.setOnClickListener { customerPresenter.onCreateCustomerButtonClicked()}

        customerPresenter.attachView(this)
        customerPresenter.initialize(salon.id)


    }


    override fun setCustomerlist(customerEntities: List<CustomerModel>?) {
        //customers_recyclerView.adapter = CustomerAdapter(this, customerEntities!!.toMutableList()) //todo
    }

    override fun showCreateCustomerlistFragment() {
         //todo
        MaterialDialog.Builder(this)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onLongItemClicked(item: CustomerModel) {
    }

    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        toast(event)
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