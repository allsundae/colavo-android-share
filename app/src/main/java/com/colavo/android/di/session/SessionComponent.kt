package com.colavo.android.di.session

import com.colavo.android.ui.login.LoginActivity
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(SessionModule::class))
@SessionScope
interface SessionComponent {

    fun inject(loginActivity: LoginActivity)

}