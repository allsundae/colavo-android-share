package com.colavo.android.base.presenter

import com.colavo.android.base.BaseView

/**
 * Created by macbookpro on 2017. 9. 14..
 */
abstract class AbstractPresenter<VIEW : BaseView> : BasePresenter<VIEW> {

    protected var view: VIEW? = null
        private set

    override fun attachView(view: VIEW) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

}