package com.colavo.android.ui.adapter

/**
 * Created by macbookpro on 2017. 9. 13..
 */
interface SectionsPagerModel {

    fun setListItem(position: Int)

    fun getListItem(position: Int): Int

    fun getCount(): Int
}