package com.colavo.android.di.app

import android.content.Context
import com.colavo.android.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun getApplicationContext(): Context = app

}