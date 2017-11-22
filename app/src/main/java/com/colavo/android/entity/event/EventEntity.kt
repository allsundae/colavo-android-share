package com.colavo.android.entity.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EventEntity(
        id: String="",
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
       @SerializedName("services")              @Expose  var services: List<ServiceMenu> = mutableListOf(ServiceMenu(0.0,0.0,0.0,"", "", "", 0.0, 0.0)),
       @SerializedName("discounts")             @Expose  var discounts: List<DiscountMenu> = mutableListOf(DiscountMenu("",false,"",0.0,0.0, 0.0)),
       @SerializedName("logs")                  @Expose  var logs: List<EventLogs> = mutableListOf(EventLogs(false))
                       ): BaseEvent(id, created_at, updated_at, begin_at, end_at, employee_only_event_title, booked_by_customer
                        , salon_key, employee_key, customer_key, memo_key, checkout_key, cancel_reason) , Serializable


data class ServiceMenu (
        @SerializedName("created_at")           @Expose var created_at: Double = 0.0,
        @SerializedName("duration_min")         @Expose var duration_min: Double = 0.0,         //e.g.  150min
        @SerializedName("uniq_duration_min")    @Expose var uniq_duration_min: Double = 0.0,    //e.g.  120min
//        @SerializedName("salon_key")          @Expose var salon_key: String = "",
        @SerializedName("key")                  @Expose var key: String = "",
        @SerializedName("name")                 @Expose var name: String = "",                  // e.g. Digital Perm
        @SerializedName("desc")                 @Expose var desc: String = "",
        @SerializedName("price")                @Expose var price: Double = 0.0,
        @SerializedName("updated_at")           @Expose var updated_at: Double = 0.0
        ) 

data class DiscountMenu (
        @SerializedName("salon_key")            @Expose var salon_key: String = "",
        @SerializedName("value_as_rate")        @Expose var value_as_rate: Boolean = false,
        @SerializedName("name")                 @Expose var name: String = "",
        @SerializedName("value")                @Expose var value: Double = 0.0,
        @SerializedName("created_at")           @Expose var created_at: Double = 0.0,
        @SerializedName("updated_at")           @Expose var updated_at: Double = 0.0
        )

data class EventLogs(
        @SerializedName("log_key")              @Expose var log_key: Boolean = false
        )
