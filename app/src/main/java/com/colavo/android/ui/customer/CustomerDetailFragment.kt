package com.colavo.android.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.ui.adapter.CustomerAdapter
import android.view.animation.AlphaAnimation
import com.flipboard.bottomsheet.commons.BottomSheetFragment


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


    override fun onItemClicked(item: CustomerModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
/*        val intent = Intent(this, CustomerDetailFragment::class.java)
        intent.putExtra(EXTRA_CUSTOMER, item)
        startActivity(intent)*/
    }

    override fun onLongItemClicked(item: CustomerModel) {
    }

    override fun onDestroy() {
        super.onDestroy()
        //clearCustomerComponent()
    }

}


