package com.colavo.android.repositories.event.datasource.mapper

import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.session.User
import org.joda.time.DateTime

/**
 * Created by RUS on 02.08.2016.
 */
class EventMapper {

    companion object {

        fun transformFromEventEntityAndUser(eventEntity: EventEntity, user: User, isEventMine: Boolean): EventModel
                = EventModel(eventEntity.id, eventEntity.text, user.name, DateTime.parse(eventEntity.time), isEventMine)

    }

}