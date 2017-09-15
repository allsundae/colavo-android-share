package com.colavo.android.base

import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife

/**
 * Created by macbookpro on 2017. 9. 14..
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this@BaseActivity)
    }
}