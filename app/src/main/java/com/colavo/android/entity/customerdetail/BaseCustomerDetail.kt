package com.colavo.android.entity.customerdetail

import java.io.Serializable


abstract class BaseCustomerDetail(
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
        open var cancel_reason: String,
        open var services: List<ServiceMenu>,
        open var discounts: List<DiscountMenu>,
        open var logs: List<EventLogs>,
        open var customer_name: String,
        open var customer_image_full_url: String,
        open var customer_image_thumb_url: String,
        open var customer_menu: String,
        open var memo: String
): Serializable