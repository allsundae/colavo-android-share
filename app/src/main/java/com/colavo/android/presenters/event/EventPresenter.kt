package com.colavo.android.presenters.event

import com.colavo.android.ui.event.eventView

/**
 * Created by RUS on 23.07.2016.
 */
interface EventPresenter {

    fun attachView(eventView: eventView)

    fun initialize(salonId: String)

    fun sendEvent(eventText: String)

    fun onDestroy()

}