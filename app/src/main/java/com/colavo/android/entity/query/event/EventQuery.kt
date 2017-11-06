package com.colavo.android.entity.query.event

import com.colavo.android.entity.query.BaseQuery
import java.sql.Timestamp

/**
 * Created by RUS on 24.07.2016.
 */
sealed class EventQuery : BaseQuery {
    class GetSalonEvents(val salonId: String) : EventQuery()
    class SendEvent(val salonId: String, val text: String, val time: String) : EventQuery()
 //   class SendEvent(val salonId: String, val text: String, val time: Map<String,String>) : EventQuery()
}