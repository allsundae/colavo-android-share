package com.colavo.android.ui

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.R.menu.menu_checkout
import com.colavo.android.R.string.bottom_navi_2
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.checkout.CheckoutPresenterImpl
import com.colavo.android.ui.adapter.CheckoutAdapter
import com.colavo.android.ui.animations.DetailsTransition
import com.colavo.android.ui.checkout.CheckoutListView
import com.colavo.android.ui.customerdetail.CustomerDetailFragment
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.checkout_item.view.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*
import com.colavo.android.utils.toast
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment02 : BaseFragment(), CheckoutListView
        , CheckoutAdapter.OnItemClickListener{

    override fun onLongItemClicked(item: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var checkoutPresenter: CheckoutPresenterImpl
    lateinit var checkoutAdapter: CheckoutAdapter

    override fun getLayout() = R.layout.fragment_02

    companion object {
        fun newInstance() = PlaceholderFragment02()
        val BUNDLE_EXTRA: String = "CHECKOUT"
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
        (context.applicationContext as App).addCheckoutComponent().inject(this)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle (bottom_navi_2)
        toolBar.inflateMenu(menu_checkout)

        setHasOptionsMenu(true)

        checkoutAdapter = CheckoutAdapter(this, mutableListOf<CheckoutModel>())
        checkout_recyclerView.adapter = checkoutAdapter

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel

        //  (application as App).addCustomerComponent().inject(this)
        //checkout_recyclerView.layoutManager = LinearLayoutManager(this.context)

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        layoutManager.scrollToPosition(0)
        checkout_recyclerView.layoutManager = layoutManager //LinearLayoutManager(this.context)

     //   val list = rootView.findViewById(R.id.list1) as RecyclerViewEmptySupport
     //   checkout_recyclerView.setLayoutManager(LinearLayoutManager(context))
        checkout_recyclerView.setEmptyView(empty_checkout)

        checkoutPresenter.attachView(this)
        checkoutPresenter.initialize(salon.id)


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_checkout_stat -> {
                //TODO checkout stat
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }
    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        context.toast(event)
    }

    override fun showSnackbar(event: String) {
    }

    override fun setCheckoutlist(customerEntities: List<CheckoutModel>?) {
    }

    override fun showCreateCheckoutlistFragment() {
    }

    override fun openCheckoutFragment(customerModel: CheckoutModel) {
    }

    override fun addCheckout(checkoutEntity: CheckoutModel) {

        checkoutAdapter.items.add(checkoutEntity)
        checkoutAdapter.notifyItemInserted(checkoutAdapter.itemCount)
        Logger.log("Checkout addCheckout : ${checkoutEntity.checkout_uid} (${checkoutAdapter.itemCount})")
    }

    override fun changeCheckout(checkoutEntity: CheckoutModel) {
        Logger.log("Checkout changed")

        val position = (checkout_recyclerView.adapter as CheckoutAdapter).items.indexOfFirst { it.checkout_uid.equals(checkoutEntity.checkout_uid) }
        (checkout_recyclerView.adapter as CheckoutAdapter).items[position] = checkoutEntity
        checkout_recyclerView.adapter.notifyItemChanged(position)
    }

    override fun removeCheckout(checkoutEntity: CheckoutModel) {
        Logger.log("Checkout removed")

        val position = checkoutAdapter.items.indexOfFirst { it.checkout_uid.equals(checkoutEntity) }
        checkoutAdapter.items.removeAt(position)
        checkoutAdapter.notifyItemRemoved(position)
    }

    override fun showProgress() {
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.hide()
    }
    override fun onItemClicked(item: CheckoutModel, position: Int, v: View) {
        Toast.makeText(context, "Clicked ${item.customer_name} : ${position}" , Toast.LENGTH_LONG).show()

        v.checkout_customer_image.buildDrawingCache()
        val bitmap = v.checkout_customer_image.getDrawingCache()
        val bs = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bs)
        val byteArray = bs.toByteArray()

        val newFragment = CustomerDetailFragment()

        val bundle = Bundle(3)
        bundle.putSerializable(PlaceholderFragment02.BUNDLE_EXTRA, item)
        bundle.putString("SENDER","checkout")
        bundle.putByteArray("BYTE", byteArray)
        newFragment.setArguments(bundle)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newFragment.setSharedElementEnterTransition(DetailsTransition())
            newFragment.setEnterTransition(android.transition.Fade())
            exitTransition = android.transition.Fade()
            newFragment.setSharedElementReturnTransition(DetailsTransition())
        }

        val transaction = fragmentManager.beginTransaction()
    //    transaction.addSharedElement(checkout_customer_name,"customer_name" )//logoTransitionName.toString()
        transaction.replace(R.id.checkout_list_holder, newFragment) //container
        transaction.addToBackStack(null)
        transaction.commit()
        //   mDelayedTransactionHandler.postDelayed(mRunnable, 1000);
    }

}