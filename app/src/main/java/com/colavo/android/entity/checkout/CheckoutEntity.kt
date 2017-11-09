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
        @SerializedName("price")                       @Expose override var price: String ="",
        @SerializedName("is_manual_price")             @Expose override var is_manual_price: Boolean = false,
        @SerializedName("reserve_fund")                @Expose override var reserve_fund: String ="",
        @SerializedName("paid_fund")                   @Expose override var paid_fund: String ="",
        @SerializedName("author_employee_key")         @Expose override var author_employee_key: String = "",
        @SerializedName("paid_types")                  @Expose override var paid_types: List<PaidType> = mutableListOf(PaidType("","","", "", "")),
        @SerializedName("created_at")                  @Expose override var created_at: String = "",
        @SerializedName("updated_at")                  @Expose override var updated_at: String ="",
        @SerializedName("reserveFund")                 @Expose override var reserveFund: String ="",
        @SerializedName("paidFund")                    @Expose override var paidFund: String ="",
        @SerializedName("tip")                         @Expose override var tip: String =""
)
    : BaseCheckout(checkout_uid, salon_key, event_key, price, is_manual_price, reserve_fund
                    ,paid_fund, author_employee_key, paid_types, created_at, updated_at,  reserveFund
                    ,paidFund, tip), Serializable



data class PaidType (
        var uid: String = "",
        @SerializedName("payment_type_key")         @Expose var payment_type_key: String = "",
        @SerializedName("name")                     @Expose var name: String = "",
        @SerializedName("price")                    @Expose var price: String ="",
        @SerializedName("reserve_fund_rate")        @Expose var reserve_fund_rate: String =""
        )
