package com.colavo.android.repositories.session.datasource

import com.colavo.android.entity.session.User
import com.colavo.android.entity.query.session.SessionQuery
import com.colavo.android.entity.query.Handle
import rx.Observable

/**
 * Created by RUS on 11.07.2016.
 */
interface SessionDataSource {

    @Handle(SessionQuery.Register::class)
    fun register(query: SessionQuery.Register): Observable<User>

    @Handle(SessionQuery.SignIn::class)
    fun signIn(query: SessionQuery.SignIn): Observable<User>

    @Handle(SessionQuery.SignOut::class)
    fun signOut(query: SessionQuery.SignOut): Observable<User>

}