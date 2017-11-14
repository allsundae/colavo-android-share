package com.colavo.android.ui

import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.transition.Fade
import android.support.transition.TransitionSet
import android.support.v4.app.BundleCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
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

import com.flipboard.bottomsheet.BottomSheetLayout
import kotlinx.android.synthetic.main.customer_detail_bottom_fragment.*
import butterknife.ButterKnife
import android.transition.TransitionInflater
import com.colavo.android.R.string.logoTransitionName
import com.colavo.android.ui.animations.DetailsTransition
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import kotlinx.android.synthetic.main.customer_item.*
import kotlinx.android.synthetic.main.customer_item.view.*
import java.io.ByteArrayOutputStream


class PlaceholderFragment04 : BaseFragment(), CustomerlistView
        , CustomerAdapter.OnItemClickListener {
    /* Transition */
    private val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300
    private val mDelayedTransactionHandler = Handler()
    private val mRunnable = Runnable { this.performTransition() }

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


    protected lateinit var bottomSheetLayout: BottomSheetLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (context.applicationContext as App).addCustomerComponent().inject(this)
   // this works     (context.applicationContext as App).addCustomerComponent().inject(this)
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


    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        val title = R.string.bottom_navi_4
        toolBar?.setTitle (title) //R.string.bottom_navi_4
        toolBar?.inflateMenu(menu_customer)
        Logger.log("TOOLBAR DISPLAYED : ${title} ${customerAdapter.itemCount}")
        //TODO update number of customers
    }

    override fun updateNumberofCustomer(){

        toolBar?.setTitle ("${R.string.bottom_navi_4} ${customerAdapter.itemCount}" )
        Logger.log("TOOLBAR UPDATED : ${customerAdapter.itemCount}")
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

        customerAdapter.items.add(customerEntity)
        customerAdapter.notifyItemInserted(customerAdapter.itemCount)
        Logger.log("Customer added : ${customerEntity.name} (${customerAdapter.itemCount})")
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

    override fun onItemClicked(item: CustomerModel, position: Int, v: View) {
        Toast.makeText(context, "Clicked ${item.name} : ${position}" , Toast.LENGTH_LONG).show()

  //      CustomerDetailFragment().show(activity.getSupportFragmentManager(), R.id.bottomsheet)

        v.customer_image.buildDrawingCache()
        val bitmap = v.customer_image.getDrawingCache()
        val bs = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bs)
        val byteArray = bs.toByteArray()



        val newFragment = CustomerDetailFragment()

        val bundle = Bundle(2)
        bundle.putSerializable(EXTRA_CUSTOMER, item)
        bundle.putByteArray("BYTE", byteArray)
        newFragment.setArguments(bundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newFragment.setSharedElementEnterTransition(DetailsTransition())
            newFragment.setEnterTransition(android.transition.Fade())
            exitTransition = android.transition.Fade()
            newFragment.setSharedElementReturnTransition(DetailsTransition())
        }

        val transaction = fragmentManager.beginTransaction()
        transaction.addSharedElement(customer_name,"customer_name" )//logoTransitionName.toString()
        transaction.replace(R.id.customer_list_holder, newFragment) //container
        transaction.addToBackStack(null)
        transaction.commit()
     //   mDelayedTransactionHandler.postDelayed(mRunnable, 1000);
    }

    private fun performTransition() {

/*        val previousFragment = fragmentManager.findFragmentById(R.id.customer_list_holder)
        val nextFragment = CustomerDetailFragment.newInstance()

        val fragmentTransaction = fragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = android.support.transition.Fade()
        exitFade.setDuration(FADE_DEFAULT_TIME)
        previousFragment.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = android.support.transition.TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
        nextFragment.setSharedElementEnterTransition(enterTransitionSet)

        // 3. Enter Transition for New Fragment
        val enterFade = android.support.transition.Fade()
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
        enterFade.setDuration(FADE_DEFAULT_TIME)
        nextFragment.setEnterTransition(enterFade)

        val logo = ButterKnife.findById(this, R.id.fragment1_logo)
        fragmentTransaction.addSharedElement(logo, logo.getTransitionName())
        fragmentTransaction.replace(R.id.fragment_container, nextFragment)
        fragmentTransaction.commitAllowingStateLoss()*/
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
        mDelayedTransactionHandler.removeCallbacks(mRunnable);
        customerPresenter.onDestroy()
    }


}
