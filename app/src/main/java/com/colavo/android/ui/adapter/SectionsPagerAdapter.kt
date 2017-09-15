package com.colavo.android.ui.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.colavo.android.ui.*
import java.util.*

/**
 * Created by macbookpro on 2017. 9. 13..
 */

class SectionsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager), SectionsPagerModel {

    private val itemList = ArrayList<Int>()

    override fun getItem(position: Int) = when (position) {
        1 -> PlaceholderFragment02.newInstance()
        2 -> PlaceholderFragment03.newInstance()
        3 -> PlaceholderFragment04.newInstance()
        4 -> PlaceholderFragment05.newInstance()
        else -> PlaceholderFragment.newInstance()
    }

/*    override fun getItem(position: Int) = when (position) {
       PlaceholderFragment.newInstance()
    }*/

    override fun getCount() = itemList.size

    override fun setListItem(position: Int) {
        itemList.add(position)
    }

    override fun getListItem(position: Int) = itemList[position]
}