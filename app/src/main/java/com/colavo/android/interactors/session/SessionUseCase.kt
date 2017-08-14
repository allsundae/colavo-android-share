package com.colavo.android.interactors.session

import com.colavo.android.entity.query.session.SessionQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.session.SessionRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by RUS on 14.07.2016.
 */
abstract class SessionUseCase (val sessionRepository: SessionRepository) : BaseUseCase(){

    protected fun <T> execute(query: SessionQuery, subscriber: Subscriber<T>)  {
        this.subscription = sessionRepository.query<T>(query)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}