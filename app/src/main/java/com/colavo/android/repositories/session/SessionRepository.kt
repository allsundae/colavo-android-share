package com.colavo.android.repositories.session

import com.colavo.android.entity.query.session.SessionQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.session.datasource.SessionDataSourceImpl
import com.colavo.android.utils.HandleUtils
import rx.Observable
import javax.inject.Singleton

/**
 * Created by RUS on 11.07.2016.
 */
@Singleton
class SessionRepository(sessionDataSourceImpl: SessionDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, sessionDataSourceImpl)
    }

    fun <T> query(sessionQuery: SessionQuery): Observable<T> = getObservable(sessionQuery)

}