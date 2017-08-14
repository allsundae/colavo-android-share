package com.colavo.android.ui.event

import com.colavo.android.entity.event.EventModel
import com.colavo.android.ui.BaseView

/**
 * Created by RUS on 23.07.2016.
 */
interface eventView : BaseView {

    fun setEvents(eventEntities: List<EventModel>)

    fun addEvent(eventEntity: EventModel)

    fun changeEvent(eventEntity: EventModel)

    fun removeEvent(eventEntity: EventModel)

}