package com.colavo.android.di.session

import com.colavo.android.ui.login.LoginActivity
import dagger.Subcomponent

/**
 * Created by RUS on 20.07.2016.
 */
@Subcomponent(modules = arrayOf(SessionModule::class))
@SessionScope
interface SessionComponent {

    fun inject(loginActivity: LoginActivity)

}