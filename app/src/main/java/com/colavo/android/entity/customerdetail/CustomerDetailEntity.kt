package com.colavo.android.entity.customerdetail

import com.colavo.android.entity.customerdetail.BaseCustomerDetail
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable


class CustomerDetailEntity(
     /*   id: String="",
       @SerializedName("created_at")            @Expose override var created_at: Double = 0.0,
       @SerializedName("updated_at")            @Expose override var updated_at: Double = 0.0,
       @SerializedName("begin_at")              @Expose override var begin_at: Double = 0.0,
       @SerializedName("end_at")                @Expose override var end_at: Double = 0.0,
       @SerializedName("employee_only_event_title")       @Expose override var employee_only_event_title: String = "",
       @SerializedName("booked_by_customer")    @Expose override var booked_by_customer: Boolean = false,
       @SerializedName("salon_key")             @Expose override var salon_key: String = "",
       @SerializedName("employee_key")          @Expose override var employee_key: String = "",
       @SerializedName("customer_key")          @Expose override var customer_key: String = "",
       @SerializedName("memo_key")              @Expose override var memo_key: String = "",
       @SerializedName("checkout_key")          @Expose override var checkout_key: String = "",
       @SerializedName("cancel_reason")         @Expose override var cancel_reason: String = "",
       @SerializedName("services")              @Expose  override var services: List<ServiceMenu> = mutableListOf(ServiceMenu(
               "",
               0.0,0.0,0.0,"", "", 0.0, "",  0.0)),
       @SerializedName("discounts")             @Expose  override var discounts: List<DiscountMenu> = mutableListOf(DiscountMenu(
               "","",false,"",0.0,0.0, 0.0)),
       @SerializedName("logs")                  @Expose override  var logs: List<EventLogs> = mutableListOf(EventLogs("")),
        @SerializedName("customer_name")          @Expose override var customer_name: String = "",
        @SerializedName("customer_image_full_url")          @Expose override var customer_image_full_url: String = "",
        @SerializedName("customer_image_thumb_url")          @Expose override var customer_image_thumb_url: String = "",
        @SerializedName("customer_menu")          @Expose override var customer_menu: String = "",
        @SerializedName("memo")                 @Expose override var memo: String = ""
                       ): BaseCustomerDetail(id, created_at, updated_at, begin_at, end_at, employee_only_event_title, booked_by_customer
                        , salon_key, employee_key, customer_key, memo_key, checkout_key, cancel_reason
                        , services, discounts, logs
                        , customer_name, customer_image_full_url, customer_image_thumb_url, customer_menu, memo
                                    */)
//, Serializable
