package com.colavo.android.ui.login

import com.colavo.android.ui.BaseView

/**
 * Created by RUS on 11.07.2016.
 */
interface LoginView : BaseView {

    fun openSalonsActivity(uid: String)

    fun showSignInProgress()

    fun hideSignInProgress()

    fun showRegisterProgress()

    fun hideRegisterProgress()

}