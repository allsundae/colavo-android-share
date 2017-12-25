package com.colavo.android.ui.checkout

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.schibsted.spain.parallaxlayerlayout.ParallaxLayerLayout

/**
 * Created by macbookpro on 2017. 12. 25..
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


class ColavoAnimatedTranslationUpdater @JvmOverloads constructor(private val maxParallax: Float = 1.0f) : ParallaxLayerLayout.TranslationUpdater {
    private var parallax: ParallaxLayerLayout? = null
    private var animation: ValueAnimator? = null

    override fun subscribe(parallaxLayerLayout: ParallaxLayerLayout) {
        this.parallax = parallaxLayerLayout
        val listener = ValueAnimator.AnimatorUpdateListener { animator -> this@ColavoAnimatedTranslationUpdater.parallax!!.updateTranslations(floatArrayOf((animator.animatedValue as Float).toFloat(), 0.0f)) }
        this.animation = ValueAnimator.ofFloat(*floatArrayOf(0.0f, this.maxParallax, 0.0f, -this.maxParallax, 0.0f))
        this.animation!!.duration = 5500L
        this.animation!!.interpolator = LinearInterpolator()
        this.animation!!.repeatCount = -1
        this.animation!!.repeatMode = ValueAnimator.REVERSE
        this.animation!!.addUpdateListener(listener)
        this.animation!!.start()
    }

    override fun unSubscribe() {
        if (this.animation != null && this.animation!!.isStarted) {
            this.animation!!.end()
        }

    }
}
