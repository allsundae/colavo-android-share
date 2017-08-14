package com.colavo.android.presenters.login

import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.login.LoginView

/**
 * Created by RUS on 11.07.2016.
 */
interface LoginPresenter : BasePresenter {

    fun attachView(loginView: LoginView)

    fun signIn(email: String, password: String)

    fun register(name: String, password: String)

    fun signOut()

}