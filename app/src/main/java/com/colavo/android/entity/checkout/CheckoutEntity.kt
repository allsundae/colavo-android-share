package com.colavo.android.entity.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rx.schedulers.TimeInterval
import java.io.Serializable
import java.util.*


class  CheckoutEntity(
        checkout_uid: String = "",
        @SerializedName("salon_key")                   @Expose override var salon_key: String = "",
        @SerializedName("event_key")                   @Expose override var event_key: String ="",
        @SerializedName("price")                       @Expose override var price: Double = 0.0,
        @SerializedName("is_manual_price")             @Expose override var is_manual_price: Boolean = false,
        @SerializedName("reserve_fund")                @Expose override var reserve_fund: Double = 0.0,
        @SerializedName("paid_fund")                   @Expose override var paid_fund: Double = 0.0,
        @SerializedName("author_employee_key")         @Expose override var author_employee_key: String = "",
        @SerializedName("paid_types")                  @Expose override var paid_types: List<PaidType> = mutableListOf(PaidType("","","", 0.0, 0.0)),
        @SerializedName("created_at")                  @Expose override var created_at: Double = 0.0,
        @SerializedName("updated_at")                  @Expose override var updated_at: Double = 0.0,
        @SerializedName("reserveFund")                 @Expose override var reserveFund: Double  = 0.0,
        @SerializedName("paidFund")                    @Expose override var paidFund: Double = 0.0,
        @SerializedName("tip")                         @Expose override var tip: Double = 0.0,
        @SerializedName("customer_key")                @Expose override var customer_key: String = "",
        @SerializedName("customer_name")                @Expose override var customer_name: String = "",
        @SerializedName("customer_image_thumb")                @Expose override var customer_image_thumb: String = "",
        @SerializedName("customer_image_full")                @Expose override var customer_image_full: String = "",
        @SerializedName("customer_menu")                @Expose override var customer_menu: String = ""
)
    : BaseCheckout(checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                    ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reserveFund
                    ,paidFund, tip, customer_key, customer_name, customer_image_thumb, customer_image_full, customer_menu), Serializable



data class PaidType (
        var uid: String = "",
        @SerializedName("payment_type_key")         @Expose var payment_type_key: String = "",
        @SerializedName("name")                     @Expose var name: String = "",
        @SerializedName("price")                    @Expose var price: Double = 0.0,
        @SerializedName("reserve_fund_rate")        @Expose var reserve_fund_rate: Double = 0.0
        )
