package com.colavo.android.interactors.event

import com.colavo.android.entity.query.event.EventQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.event.EventRepository
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 01.08.2016.
 */
@UseCase
class SendEvent @Inject constructor(eventRepository: EventRepository) : EventUseCase(eventRepository) {

    fun execute(conversationId: String, text: String, time: String, subscriber: Subscriber<FirebaseResponse>)
            = super.execute(EventQuery.SendEvent(conversationId, text, time), subscriber)
}