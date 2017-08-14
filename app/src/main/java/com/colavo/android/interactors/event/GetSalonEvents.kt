package com.colavo.android.interactors.event

import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.entity.query.event.EventQuery
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.event.EventRepository
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 01.08.2016.
 */
@UseCase
class GetSalonEvents @Inject constructor(eventRepository: EventRepository) : EventUseCase(eventRepository) {

    fun execute(conversationId: String, subscriber: Subscriber<Pair<EventModel, ResponseType>>)
            = super.execute(EventQuery.GetSalonEvents(conversationId), subscriber)

}