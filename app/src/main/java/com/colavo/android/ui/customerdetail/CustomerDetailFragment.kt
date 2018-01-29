package com.colavo.android.ui.customerdetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerModel
import android.view.animation.AlphaAnimation

import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.event.EventModel
import com.colavo.android.presenters.customerdetail.CustomerDetailPresenterImpl
import com.colavo.android.ui.PlaceholderFragment02
import com.colavo.android.ui.adapter.CustomerDetailAdapter
import com.colavo.android.utils.Logger
import com.colavo.android.utils.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_empty.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import javax.inject.Inject
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Message
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.CircleTransform
import com.colavo.android.utils.currencyFormatter
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import com.google.firebase.database.DatabaseError
import android.databinding.adapters.TextViewBindingAdapter.setText
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.repositories.customer.datasource.mapper.CustomerMapper.Companion.transformFromEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener




class CustomerDetailFragment : BaseFragment(), CustomerDetailListView
        , CustomerDetailAdapter.OnItemClickListener  {


    @Inject
    lateinit var customerdetailPresenter: CustomerDetailPresenterImpl
    lateinit var customerdetailAdapter: CustomerDetailAdapter

    var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var customer = CustomerModel()

    var customerPhone = ""
    var currentSalonUid = ""
    var currentCustomerUid = ""


    override fun getLayout() = R.layout.customer_detail_fragment

    fun getCustomerDetailName():String {
        return container_1stline.text.toString()
    }

    companion object {
//        fun newInstance() = CustomerDetailFragment()
            val EXTRA_CUSTOMER_DETAIL: String = "CUSTOMER"
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
   //     val customer = bundle.getSerializable(PlaceholderFragment04.EXTRA_CHECKOUT) as CustomerModel
        customer_detail_recyclerView.setNestedScrollingEnabled(false);
        customer_detail_recyclerView.setItemViewCacheSize(20)
        customer_detail_recyclerView.isDrawingCacheEnabled = true

        showProgress()

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
        currentSalonUid = salon.id

        if (sender == "checkout") {
         //   val checkout = bundle.getSerializable(PlaceholderFragment02.EXTRA_CHECKOUT) as CheckoutModel
            val customer = bundle.getSerializable(PlaceholderFragment02.EXTRA_CHECKOUT) as CustomerModel
            currentCustomerUid = customer.key
            var checkout = CheckoutModel()

            checkout.checkout_uid = customer.key
            checkout.customer_name = customer.name
            checkout.customer_image_full = customer.image_url.full
            checkout.customer_image_thumb = customer.image_url.thumb
            checkout.customer_key = customer.key
            checkout.customer_fund = customer.fund
            customerPhone = customer.national_phone
            Logger.log("CustomerDetailFragment : name : ${customer.name} -> ${checkout.customer_name}, ${checkout.customer_key}")


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
            container_2ndline.text = getString(R.string.fund) + " " + currencyFormatter(customer.fund)//setText(R.string.customer)
            if (checkout.customer_image_thumb != ""){
                val byteArray = bundle.getByteArray("BYTE")
                val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                container_image.setImageBitmap(decodedBitmap)

                //val transForm = CircleTransform()
                Picasso.with(context)
                        .load(checkout.customer_image_thumb)
                        .resize(280, 280)
                        .centerCrop()
                        //.placeholder(R.drawable.ic_customer_holder_person)
                  //      .transform(transForm)
                        .noPlaceholder()
                        .into(this.container_image)
            }
            toolBar.inflateMenu(R.menu.menu_customer_detail_checkout)

        }
        else {
            customer = bundle.getSerializable(PlaceholderFragment02.EXTRA_CHECKOUT) as CustomerModel
            val checkout = CheckoutModel()
            currentCustomerUid = customer.key

            checkout.checkout_uid = customer.key
            checkout.customer_name = customer.name
            checkout.customer_image_full = customer.image_url.full
            checkout.customer_image_thumb = customer.image_url.thumb
            customerPhone = customer.national_phone
            checkout.customer_key = customer.key

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
            container_2ndline.text = getString(R.string.fund) + " " + currencyFormatter(customer.fund)//setText(R.string.customer)
            if (checkout.customer_image_thumb != ""){
                val byteArray = bundle.getByteArray("BYTE")
                val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                container_image.setImageBitmap(decodedBitmap)

            //    val transForm = CircleTransform()
                Picasso.with(context)
                        .load(checkout.customer_image_thumb)
                        .resize(280, 280)
                        .centerCrop()
                        //.placeholder(R.drawable.ic_customer_holder_person)
              //          .transform(transForm)
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

        refresh(currentSalonUid, currentCustomerUid)

        toolBar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as AppCompatActivity).onBackPressed()
            }
        })

        /*val emptyViewRipple = ripplebg as RippleBackground
        if (customer_detail_empty.visibility == View.VISIBLE) emptyViewRipple.startRippleAnimation()
        */



    }

    override fun refresh(salonId: String, customerId: String) {
  //      val transaction : android.support.v4.app.FragmentTransaction? = fragmentManager?.beginTransaction()
  //      transaction?.detach(this)?.attach(this)?.commit()
  //      showSnackbar("refresh()")

        var newCustomer = CustomerEntity()
//        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
        val mDatabase = FirebaseDatabase.getInstance().getReference().child("salon_customers").child(salonId).child(customerId)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                newCustomer = dataSnapshot.getValue(CustomerEntity::class.java)!!
                customer = transformFromEntity(newCustomer)

                container_1stline?.text = newCustomer.name
                container_2ndline?.text = getString(R.string.fund) + " " + currencyFormatter(newCustomer.fund)
                customerPhone = newCustomer.national_phone

                if (newCustomer.image_url.thumb != ""){
                    Picasso.with(context)
                            .load(newCustomer.image_url.thumb)
                            .resize(280, 280)
                            .centerCrop()
                            .noPlaceholder()
                            .into(container_image)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                showSnackbar("Failed to read value." + error.toException().toString())
            }
        })

//        customerdetailAdapter.notifyDataSetChanged()




/*        val currentFragment = activity!!.fragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment instanceof "NAME OF YOUR FRAGMENT CLASS") {
            FragmentTransaction fragTransaction =   (getActivity()).getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();}
    } */
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

    private inner class YourDialogFragmentDismissHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //refresh()
            // refresh your textview's here
        }
    }



     override fun onItemClicked(item: CheckoutModel, position: Int, v:View) {
        showToast("not implemented") //To change body of created functions use File | Settings | File Templates.
/*        val intent = Intent(this, CustomerDetailFragment::class.java)
        intent.putExtra(EXTRA_CHECKOUT, item)
        startActivity(intent)*/
    }


     override fun onLongItemClicked(item: CheckoutModel) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_customer_detail, menu)

        if (customerPhone == "" ) {
            menu.removeItem(R.id.action_customer_call)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_customer_call -> {
                if (customerPhone !== "" ){
                    val uri = Uri.parse("tel:${customerPhone}")
                    val it = Intent(Intent.ACTION_DIAL, uri)
                    startActivity(it)
                    //val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerPhone, null))
                    //startActivity(intent)
                }
                else {
                    showToast(getString(R.string.err_phonenumber))
                }

                return true
            }
            R.id.action_customer_edit ->{
                showCreateCustomerFragment()
            }
            R.id.action_customer_prohibit -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

     fun showCreateCustomerFragment() {

         // for Empty screen
        // if (empty_checkout.visibility == View.VISIBLE) ripplebg!!.stopRippleAnimation()

        val newFragment = CustomerCreateFragment()
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
         val frombundle:Bundle = arguments!!

        //val customer = frombundle.getSerializable(PlaceholderFragment02.EXTRA_CHECKOUT) as CustomerModel

         container_image.buildDrawingCache()
         val bitmap = container_image.getDrawingCache()
         val bs = ByteArrayOutputStream()
         bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bs)
         val byteArray = bs.toByteArray()

         val bundle = Bundle(4)
         bundle.putSerializable(EXTRA_CUSTOMER_DETAIL, customer)
         //bundle.putSerializable(EXTRA_SALONMODDEL, salon)
         bundle.putString("SENDER","edit")
         bundle.putByteArray("BYTE", byteArray)
         newFragment.setArguments(bundle)

        val transaction = fragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.parent_enter, R.animator.parent_exit)
        transaction.replace(R.id.containerLayout, newFragment) //container
        transaction.addToBackStack(null)
        transaction.commit()
         /*                 */

    }

    override fun onDestroy() {
        super.onDestroy()
        customerdetailPresenter.onDestroy()
        //clearCustomerDetailComponent()
    }
    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun onResume() {
        super.onResume()

        //customerdetailAdapter.notifyDataSetChanged()
    }


    override fun showToast(event: String) {
        context!!.toast(event)
    }

    override fun showSnackbar(event: String) {
    }

    override fun setCustomerDetaillist(customerEntities: List<CheckoutModel>?) {
    }

    override fun showProgress() {
        empty_progress.visibility = View.VISIBLE

        val handler = Handler()
        handler.postDelayed({
            empty_group?.visibility = View.VISIBLE
            empty_progress.visibility = View.GONE
        }, 700)

        //empty_group.visibility = View.GONE
        //progressDialog.show()
    }

    override fun hideProgress() {
        empty_progress.visibility = View.GONE
        empty_group.visibility = View.VISIBLE
        //progressDialog.hide()
    }

    override fun showReceiptFragment(eventModel: EventModel) {
        showToast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMemoFragment(eventModel: EventModel) {
        showToast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCustomerDetail(customerDetailEntity: CheckoutModel) {
        hideProgress()
        customerdetailAdapter.items.add(customerDetailEntity)
        customerdetailAdapter.notifyItemInserted(customerdetailAdapter.itemCount)
        Logger.log("CustomerDetail addCustomerDetail : ${customerDetailEntity.checkout_uid} (${customerdetailAdapter.itemCount})")
    }

    override fun changeCustomerDetail(customerDetailEntity: CheckoutModel) {
        hideProgress()
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


