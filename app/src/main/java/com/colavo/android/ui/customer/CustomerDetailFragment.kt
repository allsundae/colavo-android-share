package com.colavo.android.ui.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.ui.adapter.CustomerAdapter
import android.view.animation.AlphaAnimation
import com.colavo.android.ui.PlaceholderFragment04
import com.flipboard.bottomsheet.commons.BottomSheetFragment
import kotlinx.android.synthetic.main.customer_detail_fragment.*
import android.R.attr.defaultValue
import android.R.attr.key
import android.graphics.BitmapFactory
import android.view.*
import com.colavo.android.App
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.customer_item.*
import kotlinx.android.synthetic.main.toolbar.*


class CustomerDetailFragment : BottomSheetFragment()
        , CustomerAdapter.OnItemClickListener  {

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true



    companion object {
        fun newInstance() = CustomerDetailFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.customer_detail_fragment, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
 /*       val bundle = this.arguments
        if (bundle != null) {
            val customer = bundle.getSerializable(PlaceholderFragment04.EXTRA_CUSTOMER) as CustomerModel
        }*/

      //  val bundle : Bundle = this.arguments

        val bundle:Bundle = arguments
        val customer = bundle.getSerializable(PlaceholderFragment04.EXTRA_CUSTOMER) as CustomerModel
        customer_name_detail.setText (customer.name)
        customer_phone_detail.setText(customer.phone)

        if (customer.image_urls[0].image_thumb_url !="") {

            val byteArray = bundle.getByteArray("BYTE")
            val decodedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
            customer_image_detail.setImageBitmap(decodedBitmap)

            if (customer.image_urls[0].image_full_url != "") {
                val transForm = CustomerAdapter.CircleTransform()

                Picasso.with(context)
                        .load(customer.image_urls[0].image_full_url)
                        .resize(280, 280)
                        .centerCrop()
                        //.placeholder(R.drawable.ic_customer_holder_person)
                        .transform(transForm)
                        .noPlaceholder()
                        .into(this.customer_image_detail)

            }
        }


        // Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle ("" ) //R.string.bottom_navi_4
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolBar.inflateMenu(R.menu.menu_customer_detail)

        fragmentManager.addOnBackStackChangedListener { this }
        toolBar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as AppCompatActivity).onBackPressed()
            }
        })



    }

/*
    fun onSupportNavigateUp(): Boolean {
        (activity as AppCompatActivity).onBackPressed()
        return true
    }
*/


    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }


    override fun onItemClicked(item: CustomerModel, position: Int, v:View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
/*        val intent = Intent(this, CustomerDetailFragment::class.java)
        intent.putExtra(EXTRA_CUSTOMER, item)
        startActivity(intent)*/
    }


    override fun onLongItemClicked(item: CustomerModel) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_customer_detail, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_customer_call -> {
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
        //clearCustomerComponent()
    }

}


