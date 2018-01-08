package com.colavo.android.ui.customerdetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerModel
import android.view.animation.AlphaAnimation
import com.colavo.android.ui.PlaceholderFragment04

import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.event.EventModel
import com.colavo.android.presenters.customerdetail.CustomerDetailPresenterImpl
import com.colavo.android.ui.PlaceholderFragment02
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.adapter.CustomerDetailAdapter
import com.colavo.android.utils.Logger
import com.colavo.android.utils.toast
import com.skyfishjy.library.RippleBackground
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_empty.*
import kotlinx.android.synthetic.main.base_empty.view.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import javax.inject.Inject
import android.net.Uri.fromParts
import android.content.Intent
import android.net.Uri


class CustomerDetailFragment : BaseFragment(), CustomerDetailListView
        , CustomerDetailAdapter.OnItemClickListener  {

    @Inject
    lateinit var customerdetailPresenter: CustomerDetailPresenterImpl
    lateinit var customerdetailAdapter: CustomerDetailAdapter

    var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    override fun getLayout() = R.layout.customer_detail_fragment

    fun getCustomerDetailName():String {
        return container_1stline.text.toString()
    }

    companion object {
//        fun newInstance() = CustomerDetailFragment()
        var customerPhone = ""
    }

    private val progressDialog: MaterialDialog by lazy {
        MaterialDialog.Builder(this.context!!)
                .title(R.string.conversations_loading)
                .content(R.string.wait)
                .progress(true, 0)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as App).addCustomerDetailComponent().inject(this)

        setHasOptionsMenu(true)
     //   initInstancesDrawer()
    }

    private fun initInstancesDrawer() {
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle ("") //R.string.bottom_navi_4
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolBar.inflateMenu(R.menu.menu_customer_detail)


        toolBar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as AppCompatActivity).onBackPressed()
            }
        })
    }
/*    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.customer_detail_fragment, container, false)

    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle:Bundle = arguments!!
        val sender : String = bundle.getString("SENDER")
   //     val customer = bundle.getSerializable(PlaceholderFragment04.BUNDLE_EXTRA) as CustomerModel
        customer_detail_recyclerView.setNestedScrollingEnabled(false);

        if (sender == "checkout") {
            val customer = bundle.getSerializable(PlaceholderFragment02.BUNDLE_EXTRA) as CheckoutModel
            val checkout = CheckoutModel()
            checkout.checkout_uid = customer.customer_key
            checkout.customer_name = customer.customer_name
            checkout.customer_image_full = customer.customer_image_full
            checkout.customer_image_thumb = customer.customer_image_thumb
            checkout.customer_key = customer.customer_key
            customerPhone = customer.customer_phone
            Logger.log("CustomerDetailFragment : name : ${customer.customer_name} -> ${checkout.customer_name}, ${checkout.customer_key}")


            val layoutManager = LinearLayoutManager(this.context)
            layoutManager.reverseLayout = true
            layoutManager.stackFromEnd = true
            customer_detail_recyclerView.layoutManager = layoutManager //LinearLayoutManager(this.context)

            customerdetailAdapter = CustomerDetailAdapter(this, mutableListOf<CheckoutModel>(), checkout)
            customer_detail_recyclerView.adapter = customerdetailAdapter

            customer_detail_recyclerView.setEmptyView(customer_detail_empty)
            if (customer_detail_empty.visibility == View.VISIBLE) ripplebg.startRippleAnimation()
            else ripplebg.stopRippleAnimation()

            customerdetailPresenter.attachView(this)
            customerdetailPresenter.initialize(checkout.customer_key)

            container_1stline.text = checkout.customer_name
            container_2ndline.setText(R.string.customer)
            if (checkout.customer_image_thumb != ""){
                val byteArray = bundle.getByteArray("BYTE")
                val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                container_image.setImageBitmap(decodedBitmap)

                val transForm = CustomerAdapter.CircleTransform()
                Picasso.with(context)
                        .load(checkout.customer_image_thumb)
                        .resize(280, 280)
                        .centerCrop()
                        //.placeholder(R.drawable.ic_customer_holder_person)
                        .transform(transForm)
                        .noPlaceholder()
                        .into(this.container_image)
            }
            toolBar.inflateMenu(R.menu.menu_customer_detail_checkout)

        }
        else {
            val customer = bundle.getSerializable(PlaceholderFragment04.BUNDLE_EXTRA) as CustomerModel
            val checkout = CheckoutModel()
            checkout.checkout_uid = customer.uid
            checkout.customer_name = customer.name
            checkout.customer_image_full = customer.image_urls.full
            checkout.customer_image_thumb = customer.image_urls.thumb
            customerPhone = customer.phone
            checkout.customer_key = customer.uid

            Logger.log("CustomerDetailFragment : name : ${customer.name} -> ${checkout.customer_name}, ${checkout.customer_key}")
/*
            customer_detail_recyclerView.layoutManager = LinearLayoutManager(this.context)
            customer_detail_recyclerView.adapter = customerdetailAdapter
*/
            val layoutManager = LinearLayoutManager(this.context)
            layoutManager.reverseLayout = true
            layoutManager.stackFromEnd = true
            customer_detail_recyclerView.layoutManager = layoutManager //LinearLayoutManager(this.context)

            customerdetailAdapter = CustomerDetailAdapter(this, mutableListOf<CheckoutModel>(), checkout) //mutableListOf(checkout))
            customer_detail_recyclerView.adapter = customerdetailAdapter

            customer_detail_recyclerView.setEmptyView(customer_detail_empty)
            if (customer_detail_empty.visibility == View.VISIBLE) ripplebg.startRippleAnimation()
            else ripplebg.stopRippleAnimation()

            customerdetailPresenter.attachView(this)
            customerdetailPresenter.initialize(checkout.customer_key)

            container_1stline.text = checkout.customer_name
            container_2ndline.setText(R.string.customer)
            if (checkout.customer_image_thumb != ""){
                val byteArray = bundle.getByteArray("BYTE")
                val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                container_image.setImageBitmap(decodedBitmap)

                val transForm = CustomerAdapter.CircleTransform()
                Picasso.with(context)
                        .load(checkout.customer_image_thumb)
                        .resize(280, 280)
                        .centerCrop()
                        //.placeholder(R.drawable.ic_customer_holder_person)
                        .transform(transForm)
                        .noPlaceholder()
                        .into(this.container_image)
            }

          //  toolBar.inflateMenu(R.menu.menu_customer_detail)

        }

        //
        /*
        customer_name_detail.setText(event?.customer_name)
        customer_phone_detail.setText(R.string.customers_loading)
        if (event?.customer_image_full_url != ""){
            val byteArray = bundle.getByteArray("BYTE")
            val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            customer_image_detail.setImageBitmap(decodedBitmap)

            val transForm = CustomerAdapter.CircleTransform()
            Picasso.with(context)
                    .load(event?.customer_image_full_url)
                    .resize(280, 280)
                    .centerCrop()
                    //.placeholder(R.drawable.ic_customer_holder_person)
                    .transform(transForm)
                    .noPlaceholder()
                    .into(this.customer_image_detail)
        }*/


        // Toolbar
       (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle ("") //R.string.bottom_navi_4
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)



   //     fragmentManager.addOnBackStackChangedListener { this }
        toolBar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as AppCompatActivity).onBackPressed()
            }
        })

        /*val emptyViewRipple = ripplebg as RippleBackground
        if (customer_detail_empty.visibility == View.VISIBLE) emptyViewRipple.startRippleAnimation()
        */



    }


    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }




     override fun onItemClicked(item: CheckoutModel, position: Int, v:View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
/*        val intent = Intent(this, CustomerDetailFragment::class.java)
        intent.putExtra(BUNDLE_EXTRA, item)
        startActivity(intent)*/
    }


     override fun onLongItemClicked(item: CheckoutModel) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_customer_detail, menu)

        if (customerPhone == "" ) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerPhone, null))
            menu.removeItem(R.id.action_customer_call)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_customer_call -> {
                if (customerPhone !== "" ){
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerPhone, null))
                    startActivity(intent)
                }
                else {
                    showToast(getString(R.string.err_phonenumber))
                }

                return true
            }
            R.id.action_customer_edit ->{
                return true
            }
            R.id.action_customer_prohibit -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        customerdetailPresenter.onDestroy()
        //clearCustomerDetailComponent()
    }
    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        context!!.toast(event)
    }

    override fun showSnackbar(event: String) {
    }

    override fun setCustomerDetaillist(customerEntities: List<CheckoutModel>?) {
    }

    override fun showProgress() {
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.hide()
    }

    override fun showReceiptFragment(eventModel: EventModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMemoFragment(eventModel: EventModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCustomerDetail(customerDetailEntity: CheckoutModel) {
        customerdetailAdapter.items.add(customerDetailEntity)
        customerdetailAdapter.notifyItemInserted(customerdetailAdapter.itemCount)
        Logger.log("CustomerDetail addCustomerDetail : ${customerDetailEntity.checkout_uid} (${customerdetailAdapter.itemCount})")
    }

    override fun changeCustomerDetail(customerDetailEntity: CheckoutModel) {
        Logger.log("CustomerDetail changed")

        val position = (checkout_recyclerView.adapter as CustomerDetailAdapter).items.indexOfFirst { it.checkout_uid.equals(customerDetailEntity.checkout_uid) }
        (checkout_recyclerView.adapter as CustomerDetailAdapter).items[position] = customerDetailEntity
        //checkout_recyclerView.adapter.notifyItemChanged(position)
        checkout_recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeCustomerDetail(customerDetailEntity: CheckoutModel) {
        Logger.log("CustomerDetail removed")

/*        val position = customerdetailAdapter.items.indexOfFirst { it.checkout_uid.equals(customerDetailEntity) }
        customerdetailAdapter.items.removeAt(position)
        customerdetailAdapter.notifyItemRemoved(position)*/
        customerdetailAdapter.items.removeAll{it.checkout_uid.equals(customerDetailEntity.checkout_uid)}
        customerdetailAdapter.notifyDataSetChanged()
    }
}


