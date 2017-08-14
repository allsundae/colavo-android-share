package com.colavo.android.interactors.event

import com.colavo.android.entity.query.event.EventQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.event.EventRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers

/**
 * Created by RUS on 23.07.2016.
 */
abstract class EventUseCase(val eventRepository: EventRepository) : BaseUseCase() {

    fun <T> execute(query: EventQuery, subscriber: Subscriber<T> = Subscribers.empty())  {
        this.subscription = eventRepository.query<T>(query)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

}