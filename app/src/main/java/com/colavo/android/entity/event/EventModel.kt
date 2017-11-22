package com.colavo.android.entity.event

import java.io.Serializable


class EventModel(
        id: String = "",
        created_at: Double = 0.0,
        updated_at: Double = 0.0,
        begin_at: Double = 0.0,
        end_at: Double = 0.0,
        employee_only_event_title: String = "",
        booked_by_customer: Boolean = false,
        salon_key: String = "",
        employee_key: String = "",
        customer_key: String = "",
        memo_key: String = "",
        checkout_key: String = "",
        cancel_reason: String = "",
        var services: List<ServiceMenu>  = mutableListOf(ServiceMenu(0.0,0.0,0.0,"", "", "", 0.0, 0.0)),
        var discounts: List<DiscountMenu> = mutableListOf(DiscountMenu("",false,"",0.0,0.0, 0.0)),
        var logs: List<EventLogs> = mutableListOf(EventLogs(false))
) : BaseEvent(id, created_at, updated_at, begin_at, end_at
                                , employee_only_event_title
                                , booked_by_customer, salon_key, employee_key, customer_key
                                , memo_key, checkout_key, cancel_reason), Serializable