package com.colavo.android.interactors.checkout

import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.checkout.CheckoutRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers

abstract class CheckoutUseCase(val checkoutRepository: CheckoutRepository) : BaseUseCase() {

    protected fun <T> execute(query: CheckoutQuery, subscriber: Subscriber<T> = Subscribers.empty()) {
        this.subscription = checkoutRepository.query<T>(query)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}