package com.colavo.android.entity.customerdetail

import java.io.Serializable


class CustomerDetailModel(
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
        services: List<ServiceMenu>  = mutableListOf(ServiceMenu("", 0.0,0.0,0.0,"", "", 0.0, "",  0.0)),
        discounts: List<DiscountMenu> = mutableListOf(DiscountMenu("","",false,"",0.0,0.0, 0.0)),
        logs: List<EventLogs> = mutableListOf(EventLogs("")),
        customer_name: String ="",
        customer_image_full_url: String ="",
        customer_image_thumb_url: String ="",
        customer_menu: String ="",
        memo: String =""
) : BaseCustomerDetail(id, created_at, updated_at, begin_at, end_at
                                , employee_only_event_title
                                , booked_by_customer, salon_key, employee_key, customer_key
                                , memo_key, checkout_key, cancel_reason
                                , services, discounts, logs
                                , customer_name, customer_image_full_url, customer_image_thumb_url, customer_menu, memo), Serializable