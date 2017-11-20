package com.colavo.android.repositories.event.datasource.mapper

import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.event.*


class EventMapper {

    companion object {

        fun createEventWithEventAndUser(baseEvent: BaseEvent, customer: CustomerEntity): EventModel {   //, service: ServiceMenu, discount: DiscountMenu, log: EventLogs
            val eventModel = EventModel()
            eventModel.id = baseEvent.id
            eventModel.created_at = baseEvent.created_at
            eventModel.updated_at = baseEvent.updated_at
            eventModel.begin_at = baseEvent.begin_at
            eventModel.end_at = baseEvent.end_at
            eventModel.employee_only_event_title = baseEvent.employee_only_event_title
            eventModel.booked_by_customer = baseEvent.booked_by_customer
            eventModel.salon_key = baseEvent.salon_key
            eventModel.employee_key = baseEvent.employee_key
            eventModel.customer_key = baseEvent.customer_key
            eventModel.memo_key = baseEvent.memo_key
            eventModel.checkout_key = baseEvent.checkout_key
            eventModel.cancel_reason = baseEvent.cancel_reason
          /*  eventModel.services = service.get(0).name.toString()
            eventModel.discounts = baseEvent.discounts
            eventModel.logs = baseEvent.logs*/
            return eventModel
        }


    }

}