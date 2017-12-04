package com.colavo.android.base.view

import android.content.Context
import android.os.Bundle
import com.colavo.android.base.BaseActivity
import com.colavo.android.base.BaseView
import com.colavo.android.base.presenter.BasePresenter
import com.tsengvn.typekit.TypekitContextWrapper

/**
 * Created by macbookpro on 2017. 9. 14..
 */
abstract class BasePresenterActivity<in VIEW: BaseView, P : BasePresenter<VIEW>> : BaseActivity() {

    protected var presenter: P? = null
        private set

    abstract fun onCreatePresenter(): P?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = onCreatePresenter()
        presenter?.attachView(this as VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.detachView()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase))
    }
}
