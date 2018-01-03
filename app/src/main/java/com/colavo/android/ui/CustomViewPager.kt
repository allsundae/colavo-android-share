package com.colavo.android.ui

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by macbookpro on 2017. 12. 18..
 */
public class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    internal var enabled: Boolean = false

    init {
        this.enabled = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}