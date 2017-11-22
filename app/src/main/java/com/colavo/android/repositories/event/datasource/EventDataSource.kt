package com.colavo.android.repositories.event.datasource

import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.query.Handle
import com.colavo.android.entity.query.event.EventQuery
import rx.Observable

/**
 * Created by RUS on 23.07.2016.
 */
interface EventDataSource {

    @Handle(EventQuery.GetSalonEvents::class)
    fun getEventMessages(query: EventQuery.GetSalonEvents): Observable<Pair<EventModel, ResponseType>>

    @Handle(EventQuery.SendEvent::class)
    fun sendEvent(query: EventQuery.SendEvent)

}