package com.colavo.android.base

import android.graphics.RectF
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by macbookpro on 2017. 9. 13..
 */
abstract class BaseFragment : Fragment()   {

    abstract fun getLayout(): Int



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(getLayout(), container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.let {
            ButterKnife.bind(this@BaseFragment, it)
        }
    }



}