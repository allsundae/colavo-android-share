package com.colavo.android.di.session

import com.google.firebase.auth.FirebaseAuth
import com.colavo.android.interactors.session.*
import com.colavo.android.repositories.session.SessionRepository
import com.colavo.android.repositories.session.datasource.SessionDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by RUS on 20.07.2016.
 */
@Module
class SessionModule() {

    @Provides
    @SessionScope
    fun getSignIn(sessionRepository: SessionRepository): SignIn = SignIn(sessionRepository)

    @Provides
    @SessionScope
    fun getSignOut(sessionRepository: SessionRepository): SignOut = SignOut(sessionRepository)

    @Provides
    @SessionScope
    fun getRegister(sessionRepository: SessionRepository): Register = Register(sessionRepository)

    @Provides
    @SessionScope
    fun getSessionRepository(sessionDataSourceImpl: SessionDataSourceImpl): SessionRepository = SessionRepository(sessionDataSourceImpl)

    @Provides
    @SessionScope
    fun getSessionDataSourceImpl(retrofit: Retrofit, firebaseAuth: FirebaseAuth): SessionDataSourceImpl = SessionDataSourceImpl(retrofit, firebaseAuth)

}