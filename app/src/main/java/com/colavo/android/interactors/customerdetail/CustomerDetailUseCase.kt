package com.colavo.android.interactors.customerdetail

import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.interactors.BaseUseCase
import com.colavo.android.repositories.customerdetail.CustomerDetailRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers

abstract class CustomerDetailUseCase(val customerDetailRepository: CustomerDetailRepository) : BaseUseCase() {

    protected fun<T> execute(query: CustomerDetailQuery, subscriber:Subscriber<T>  =Subscribers.empty()) {
            this.subscription=customerDetailRepository.query<T>(query)
            .onBackpressureBuffer(10000)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
            }
}
