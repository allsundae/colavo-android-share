package com.colavo.android.ui.customerdetail

import android.app.Dialog
import android.content.DialogInterface
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
import java.io.ByteArrayOutputStream
import android.graphics.Paint
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.text.InputType
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.repositories.customer.datasource.mapper.CustomerMapper.Companion.transformFromEntity
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.base_empty.*
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import kotlinx.android.synthetic.main.customer_item.*
import kotlinx.android.synthetic.main.fragment_02.*
import kotlinx.android.synthetic.main.toolbar.*


class CustomerDetailFragment : BaseFragment(), CustomerDetailListView
        , CustomerDetailAdapter.OnItemClickListener  {


    @Inject
    lateinit var customerdetailPresenter: CustomerDetailPresenterImpl
    lateinit var customerdetailAdapter: CustomerDetailAdapter

    var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var customer = CustomerModel()
    private var mCustomerEntity = CustomerEntity()

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

        customer_detail_recyclerView.isNestedScrollingEnabled = false;
        customer_detail_recyclerView.setItemViewCacheSize(20)
        customer_detail_recyclerView.isDrawingCacheEnabled = true

        showProgress()

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
        currentSalonUid = salon.id

            customer = bundle.getSerializable(PlaceholderFragment02.EXTRA_CHECKOUT) as CustomerModel
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

            var m1stlineText = container_1stline
            var m2ndlineText = container_2ndline

            if (m1stlineText == null) {
                m1stlineText = (activity as AppCompatActivity).findViewById(R.id.container_1stline) as TextView
            }
            if (m2ndlineText == null) {
                m2ndlineText = (activity as AppCompatActivity).findViewById(R.id.container_2ndline) as TextView
            }

            m1stlineText.text = checkout.customer_name
            m2ndlineText.text = (activity as AppCompatActivity).getString(R.string.fund) + " " + currencyFormatter(customer.fund)//setText(R.string.customer)

            if (customer.is_removed == true){
                m1stlineText.setPaintFlags(m1stlineText.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                m2ndlineText.text = (activity as AppCompatActivity).getString(R.string.customer_prohibited)
            }

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


    }

    override fun refresh(salonId: String, customerId: String) {
        val mDatabase = FirebaseDatabase.getInstance().getReference().child("salon_customers").child(salonId).child(customerId)

        var m1stlineText = container_1stline
        var m2ndlineText = container_2ndline
        var mCustomerImage = container_image

        if (m1stlineText == null) {
            m1stlineText = (activity as AppCompatActivity).findViewById(R.id.container_1stline) as TextView
        }
        if (m2ndlineText == null) {
            m2ndlineText = (activity as AppCompatActivity).findViewById(R.id.container_2ndline) as TextView
        }
        if (mCustomerImage == null) {
            mCustomerImage = (activity as AppCompatActivity).findViewById(R.id.container_image) as CircleImageView
        }

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mCustomerEntity = dataSnapshot.getValue(CustomerEntity::class.java)!!
                customer = transformFromEntity(mCustomerEntity)

                val activity = activity
                if (activity != null && isAdded) {
                    m1stlineText.text = mCustomerEntity.name

                    customerPhone = mCustomerEntity.national_phone

                    if (customer.is_removed == true) {
                        m1stlineText.paintFlags = m1stlineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        m1stlineText.setTextColor(resources.getColor(R.color.secondaryTextColor))
                        m2ndlineText.text = (activity as AppCompatActivity).getString(R.string.customer_prohibited)
                    } else {
                        m1stlineText.paintFlags = m1stlineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        m1stlineText.setTextColor(resources.getColor(R.color.primaryTextColor))
                        m2ndlineText.text = "${(activity as AppCompatActivity).getString(R.string.fund)} ${currencyFormatter(mCustomerEntity.fund)}"
                    }

                    if (mCustomerEntity.image_url.thumb != "") {
                        Picasso.with(context)
                                .load(mCustomerEntity.image_url.thumb)
                                .resize(280, 280)
                                .centerCrop()
                                .noPlaceholder()
                                .into(mCustomerImage)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                showSnackbar("Failed to read value." + error.toException().toString())
            }
        })
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

        if (customer.is_removed) {
            menu.removeItem(R.id.action_customer_prohibit)
        } else {
            menu.removeItem(R.id.action_customer_prohibit_cancel)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        //menu.clear()
        activity?.invalidateOptionsMenu()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_customer_call -> {
                if (customerPhone !== "" ){
                    val uri = Uri.parse("tel:${customerPhone}")
                    val it = Intent(Intent.ACTION_DIAL, uri)
                    startActivity(it)
                }
                else {
                    showToast((activity as AppCompatActivity).getString(R.string.err_phonenumber))
                }

                return true
            }
            R.id.action_customer_edit ->{
                showCreateCustomerFragment()
            }
            R.id.action_customer_prohibit -> {
                showProhibitDialog(true)
            }
            R.id.action_customer_prohibit_cancel -> {
                showProhibitDialog(false)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showProhibitDialog(isProhibit : Boolean) {
        val newCustomerEntity = mCustomerEntity
        newCustomerEntity.is_removed = isProhibit
        Logger.log("showProhibitDialog : ${newCustomerEntity.toString()}")

        if (isProhibit) {
            if (customerdetailAdapter.itemCount != 0) {
                MaterialDialog.Builder(context!!)
                        .title(R.string.action_prohibit)
                        .content(R.string.action_prohibit_body)
                        .positiveText(R.string.action_prohibit)
                        .negativeText(R.string.action_cancel)
                        .onPositive(MaterialDialog.SingleButtonCallback() { dialog, which ->
                            updateUser (currentCustomerUid, mCustomerEntity)
                        })
                        .onNegative(MaterialDialog.SingleButtonCallback() { dialog, which ->
                            dialog.dismiss()
                        })
                        .show()
            }else{ //when customer doesn't have any events
                MaterialDialog.Builder(context!!)
                        .title(R.string.action_prohibit)
                        .content(R.string.action_prohibit_body_with_reservation)
                        .positiveText(R.string.action_prohibit)
                        .negativeText(R.string.action_cancel)
                        .onPositive(MaterialDialog.SingleButtonCallback() { dialog, which ->
                            updateUser (currentCustomerUid, mCustomerEntity)
                        })
                        .onNegative(MaterialDialog.SingleButtonCallback() { dialog, which ->
                            dialog.dismiss()
                        })
                        .show()
            }
        } else {//Cancel prohibit
                        updateUser (currentCustomerUid, mCustomerEntity)
        }
    }

    private fun updateUser(customerKey: String, customerEntity: CustomerEntity) {
        Logger.log ("updateUser : ${currentSalonUid} : ${customerKey} "
                 +  "\n ${customerEntity.toString()}")

        val mDatabase = FirebaseDatabase.getInstance().getReference().child("salon_customers").child(currentSalonUid)

        mDatabase?.child(customerKey)?.setValue(customerEntity)?.addOnSuccessListener {
            showToast(getString(R.string.update_succeed))
        }?.addOnFailureListener {
            showToast(getString(R.string.update_failed))
        }

    }


    private fun showCreateCustomerFragment() {

        val newFragment = CustomerCreateFragment()
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
         val frombundle:Bundle = arguments!!

         container_image.buildDrawingCache()
         val bitmap = container_image.drawingCache
         val bs = ByteArrayOutputStream()
         bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bs)
         val byteArray = bs.toByteArray()

         val bundle = Bundle(4)
         bundle.putSerializable(EXTRA_CUSTOMER_DETAIL, customer)
         bundle.putString("SENDER","edit")
         bundle.putByteArray("BYTE", byteArray)
        newFragment.arguments = bundle

        val transaction = fragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.parent_enter, R.animator.parent_exit)
        transaction.replace(R.id.containerLayout, newFragment) //container
    //    transaction.addSharedElement(container_image, "customerImage")
        transaction.addToBackStack(null)
        transaction.commit()

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
        refresh(currentSalonUid,currentCustomerUid)
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
            empty_progress?.visibility = View.GONE
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


