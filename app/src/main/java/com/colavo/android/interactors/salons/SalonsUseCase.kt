package com.colavo.android.interactors.salons

import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.salons.SalonsRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers

/**
 * Created by RUS on 17.07.2016.
 */
abstract class SalonsUseCase(val salonsRepository: SalonsRepository) : BaseUseCase() {

    protected fun <T> execute(query: SalonsQuery, subscriber: Subscriber<T> = Subscribers.empty()) {
        this.subscription = salonsRepository.query<T>(query)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}