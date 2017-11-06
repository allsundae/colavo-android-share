package com.colavo.android.common

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by macbookpro on 2017. 10. 25..
 */
class MyTextView : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setColors(textColor: Int, accentColor: Int, backgroundColor: Int) {
        setTextColor(textColor)
        setLinkTextColor(accentColor)
    }
}