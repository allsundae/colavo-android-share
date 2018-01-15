package com.colavo.android.entity.customerdetail

import com.colavo.android.entity.checkout.DiscountMenu
import com.colavo.android.entity.checkout.ServiceMenu
import java.io.Serializable
import java.util.*


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
        ervices: HashMap<String, ServiceMenu> = hashMapOf("" to ServiceMenu(
           0.0,0.0,0.0,"", "", 0.0, "", "", 0.0)),
        iscounts: HashMap<String, DiscountMenu> = hashMapOf("" to DiscountMenu("",false,"","",0.0,0.0, 0.0)),
        ogs: HashMap<String, Boolean> = hashMapOf("" to false),
        customer_name: String ="",
        customer_image_full_url: String ="",
        customer_image_thumb_url: String ="",
        customer_menu: String ="",
        memo: String =""
) : BaseCustomerDetail(id, created_at, updated_at, begin_at, end_at
                                , employee_only_event_title
                                , booked_by_customer, salon_key, employee_key, customer_key
                                , memo_key, checkout_key, cancel_reason), Serializable