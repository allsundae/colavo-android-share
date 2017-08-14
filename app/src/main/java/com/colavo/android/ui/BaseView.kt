package com.colavo.android.ui

/**
 * Created by RUS on 22.07.2016.
 */
interface BaseView {

    fun onError(throwable: Throwable)

    fun showToast(event: String)

    fun showSnackbar(event: String)

}