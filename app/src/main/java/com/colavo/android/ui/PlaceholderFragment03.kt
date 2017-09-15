package com.colavo.android.ui

import android.os.Bundle
import android.view.View
import com.colavo.android.R
import com.colavo.android.base.BaseFragment

/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment03 : BaseFragment() {

/*    override fun getLayout(position: Int) = when (position) {
        1 -> R.layout.fragment_01
        2 -> R.layout.fragment_02
        3 -> R.layout.fragment_03
        4 -> R.layout.fragment_04
        else -> R.layout.fragment_05
    }*/
    override fun getLayout() = R.layout.fragment_03

    companion object {
        fun newInstance() = PlaceholderFragment03()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}