package com.colavo.android.interactors

import rx.Subscription
import rx.subscriptions.Subscriptions

/**
 * Created by RUS on 20.07.2016.
 */
abstract class BaseUseCase {

    protected var subscription: Subscription = Subscriptions.empty()

    fun unsubscribe() = subscription.unsubscribe()
}