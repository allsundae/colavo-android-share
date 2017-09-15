package com.colavo.android.base.presenter

import com.colavo.android.base.BaseView


/**
 * Created by macbookpro on 2017. 9. 14..
 */
interface BasePresenter<in VIEW : BaseView> {

    /**
     * View Attach.
     */
    fun attachView(view: VIEW)

    /**
     * View detach
     */
    fun detachView()
}