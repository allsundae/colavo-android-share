package com.colavo.android.entity.query.session

import com.colavo.android.entity.query.BaseQuery

/**
 * Created by RUS on 12.07.2016.
 */
sealed class SessionQuery : BaseQuery {
    class SignIn(val email: String, val password: String) : SessionQuery()
    class Register(val email: String, val name: String, val password: String) : SessionQuery()
    class SignOut : SessionQuery()
}