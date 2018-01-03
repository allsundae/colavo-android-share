package com.colavo.android.entity.event

import java.io.Serializable


abstract class BaseEvent(
        var id: String,
        open var created_at: Double,
        open var updated_at: Double,
        open var begin_at: Double,
        open var end_at: Double,
        open var employee_only_event_title: String,
        open var booked_by_customer: Boolean,
        open var salon_key: String,
        open var employee_key: String,
        open var customer_key: String,
        open var memo_key: String,
        open var checkout_key: String,
        open var cancel_reason: String
): Serializable