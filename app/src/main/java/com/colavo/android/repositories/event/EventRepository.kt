package com.colavo.android.repositories.event

import com.colavo.android.entity.query.event.EventQuery
import com.colavo.android.repositories.BaseRepository
import com.colavo.android.repositories.event.datasource.EventDataSourceImpl
import com.colavo.android.utils.HandleUtils
import rx.Observable
import javax.inject.Inject

/**
 * Created by RUS on 23.07.2016.
 */
class EventRepository @Inject constructor(dataSourceImpl: EventDataSourceImpl) : BaseRepository() {

    init {
        HandleUtils.registerHandlers(this, dataSourceImpl)
    }

    fun <T> query(eventQuery: EventQuery): Observable<T> = getObservable(eventQuery)
}