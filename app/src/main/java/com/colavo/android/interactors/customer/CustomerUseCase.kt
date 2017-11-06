package com.colavo.android.interactors.customer

import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.repositories.salons.SalonsRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers

abstract class CustomerUseCase(val customerRepository: CustomerRepository) : BaseUseCase() {

    protected fun <T> execute(query: CustomerQuery, subscriber: Subscriber<T> = Subscribers.empty()) {
        this.subscription = customerRepository.query<T>(query)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}