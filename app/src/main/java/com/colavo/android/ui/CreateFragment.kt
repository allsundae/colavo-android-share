package com.colavo.android.ui

import android.app.Dialog
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.colavo.android.R
import android.widget.RelativeLayout
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment



/**
 * Created by macbookpro on 2017. 9. 13..
 */
class CreateFabFragment : AAH_FabulousFragment() {
    var button_close: Button? = null
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.fragment_create, null)
        val rl_content = contentView.findViewById(R.id.rl_content) as RelativeLayout
        val ll_buttons = contentView.findViewById(R.id.ll_buttons) as LinearLayout
        contentView.findViewById(R.id.button_close).setOnClickListener { closeFilter("closed") }

        //params to set
        setAnimationDuration(500) //optional; default 500ms
        setPeekHeight(300) // optional; default 400dp
//        setCallbacks(activity as Callbacks) //optional; to get back result
//        setAnimationListener(activity as AAH_FabulousFragment.AnimationListener) //optional; to get animation callbacks
        setViewgroupStatic(ll_buttons) // optional; layout to stick at bottom on slide
        setViewMain(rl_content) //necessary; main bottomsheet view
        setMainContentView(contentView) // necessary; call at end before super
        super.setupDialog(dialog, style) //call super at last
    }

    companion object {

        fun newInstance(): CreateFabFragment {
            return CreateFabFragment()
        }
    }

}

