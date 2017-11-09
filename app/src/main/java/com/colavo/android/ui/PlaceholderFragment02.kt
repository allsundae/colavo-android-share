package com.colavo.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.R.menu.menu_checkout
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.R.string.bottom_navi_2
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.checkout.CheckoutPresenterImpl
import com.colavo.android.ui.adapter.CheckoutAdapter
import com.colavo.android.ui.checkout.CheckoutlistView
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_01.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment02 : BaseFragment(), CheckoutlistView
        , CheckoutAdapter.OnItemClickListener{
    override fun onItemClicked(item: CheckoutModel, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongItemClicked(item: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var checkoutPresenter: CheckoutPresenterImpl
    lateinit var checkoutAdapter: CheckoutAdapter

    override fun getLayout() = R.layout.fragment_02

    companion object {
        fun newInstance() = PlaceholderFragment02()
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

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle (bottom_navi_2)
        toolBar.inflateMenu(menu_checkout)

     //   val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
     //   (activity as AppCompatActivity).supportActionBar?.setTitle (salon.name)
        setHasOptionsMenu(true)


        checkoutAdapter = CheckoutAdapter(this, mutableListOf<CheckoutModel>())
        checkout_recyclerView.adapter = checkoutAdapter

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel

        //  (application as App).addCustomerComponent().inject(this)
        checkout_recyclerView.layoutManager = LinearLayoutManager(this.context)

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
    }

    override fun showToast(event: String) {
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
        Logger.log("Checkout added :  " + checkoutEntity.checkout_uid)

        checkoutAdapter.items.add(checkoutEntity)
        checkoutAdapter.notifyItemInserted(checkoutAdapter.itemCount)
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

}