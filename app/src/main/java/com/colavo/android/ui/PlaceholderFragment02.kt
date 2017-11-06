package com.colavo.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.*
import com.colavo.android.R
import com.colavo.android.R.menu.menu_checkout
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.R.string.bottom_navi_2
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.salons.SalonListActivity
import kotlinx.android.synthetic.main.fragment_01.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment02 : BaseFragment() {

    /*    override fun getLayout(position: Int) = when (position) {
            1 -> R.layout.fragment_01
            2 -> R.layout.fragment_02
            3 -> R.layout.fragment_03
            4 -> R.layout.fragment_04
            else -> R.layout.fragment_05
        }*/
    override fun getLayout() = R.layout.fragment_02

    companion object {
        fun newInstance() = PlaceholderFragment02()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle (bottom_navi_2)
        toolBar.inflateMenu(menu_checkout)

     //   val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
     //   (activity as AppCompatActivity).supportActionBar?.setTitle (salon.name)
        setHasOptionsMenu(true)
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

}