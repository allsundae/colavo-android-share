package com.colavo.android.entity.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rx.schedulers.TimeInterval
import java.io.Serializable
import java.util.*


class CheckoutEntity(
        uid: String = "",
        @SerializedName("salon_key")                   @Expose override var salon_key: String = "",
        @SerializedName("event_key")                   @Expose override var event_key: String ="",
        @SerializedName("price")                       @Expose override var price: Double = 0.0,
        @SerializedName("is_manual_price")             @Expose override var is_manual_price: Boolean = false,
        @SerializedName("reserve_fund")                @Expose override var reserve_fund: Double = 0.0,
        @SerializedName("paid_fund")                   @Expose override var paid_fund: Double = 0.0,
        @SerializedName("author_employee_key")         @Expose override var author_employee_key: String = "",
        @SerializedName("paid_types")                   @Expose override var paid_types: List<PaidType> = mutableListOf(PaidType("","","", 0.0, 0.0)),
        @SerializedName("salon_key")                   @Expose override var created_at: String ="",
        @SerializedName("employee_key")                @Expose override var updated_at: String = "",
        @SerializedName("customer_key")                @Expose override var reservedFund: Double = 0.0,
        @SerializedName("memo_key")                    @Expose override var paidFund: Double = 0.0,
        @SerializedName("checkout_key")                @Expose override var tip: Double = 0.0
)
    : BaseCheckout(uid, salon_key, event_key, price, is_manual_price, reserve_fund
                    ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reservedFund
                    ,paidFund, tip), Serializable



data class PaidType (
        var uid: String = "",
        @SerializedName("payment_type_key")         @Expose var payment_type_key: String = "",
        @SerializedName("name")                     @Expose var name: String = "",
        @SerializedName("price")                    @Expose var price: Double = 0.0,
        @SerializedName("reserve_fund_rate")        @Expose var reserve_fund_rate: Double = 0.0
        )
