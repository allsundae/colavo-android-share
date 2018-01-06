package com.colavo.android.entity.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rx.schedulers.TimeInterval
import java.io.Serializable
import java.util.*


class  PaidoutEntity(
        @SerializedName("created_at")            @Expose  var created_at: Double = 0.0,
        @SerializedName("updated_at")            @Expose  var updated_at: Double = 0.0,
        @SerializedName("price")                       @Expose  var price: Double = 0.0,
        @SerializedName("is_manual_price")             @Expose  var is_manual_price: Boolean = false,
        @SerializedName("reserve_fund")                @Expose  var reserve_fund: Double = 0.0,
        @SerializedName("paid_fund")                   @Expose  var paid_fund: Double = 0.0,
        @SerializedName("paid_types")                  @Expose  var paid_types: HashMap<String, PaidType> = hashMapOf("" to PaidType("","",0.0, 0.0)),
        @SerializedName("reserveFund")                 @Expose  var reserveFund: Double  = 0.0,
        @SerializedName("author_employee_key")         @Expose  var author_employee_key: String = "",
        @SerializedName("paidFund")                    @Expose  var paidFund: Double = 0.0,
        @SerializedName("tip")                    @Expose  var tip: Double = 0.0

):  Serializable


data class PaidType (
        @SerializedName("payment_type_key")         @Expose var payment_type_key: String = "",
        @SerializedName("name")                     @Expose var name: String = "",
        @SerializedName("price")                    @Expose var price: Double = 0.0,
        @SerializedName("reserve_fund_rate")        @Expose var reserve_fund_rate: Double = 0.0
        )
