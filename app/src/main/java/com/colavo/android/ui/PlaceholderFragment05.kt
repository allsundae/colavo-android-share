package com.colavo.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.colavo.android.R
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.R.string.bottom_navi_5
import com.colavo.android.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment05 : BaseFragment() {

/*    override fun getLayout(position: Int) = when (position) {
        1 -> R.layout.fragment_01
        2 -> R.layout.fragment_02
        3 -> R.layout.fragment_03
        4 -> R.layout.fragment_04
        else -> R.layout.fragment_05
    }*/
    override fun getLayout() = R.layout.fragment_05

    companion object {
        fun newInstance() = PlaceholderFragment05()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle (bottom_navi_5)
        toolBar?.inflateMenu(menu_customer)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_customer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_create_customer -> {
                //TODO create a customer
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }
}