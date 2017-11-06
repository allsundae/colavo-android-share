package com.colavo.android.ui.animations

import android.content.Context
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.util.AttributeSet

/**
 * Created by macbookpro on 2017. 11. 6..
 */
class DetailsTransition : TransitionSet {
    constructor() {
        init()
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        ordering = TransitionSet.ORDERING_TOGETHER
        addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
    }
}
