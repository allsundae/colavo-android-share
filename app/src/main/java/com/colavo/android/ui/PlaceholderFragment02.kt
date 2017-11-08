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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(event: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSnackbar(event: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCheckoutlist(customerEntities: List<CheckoutModel>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCreateCheckoutlistFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openCheckoutFragment(customerModel: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCheckout(customerEntity: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeCheckout(customerEntity: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCheckout(customerEntity: CheckoutModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}