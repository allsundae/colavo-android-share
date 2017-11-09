package com.colavo.android.repositories.checkout.datasource.mapper

import com.colavo.android.entity.checkout.BaseCheckout
import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel


class CheckoutMapper {

    companion object {

        fun createCheckoutWithEventAndUser(baseCheckout: BaseCheckout, checkout: CheckoutEntity?): CheckoutModel { //eventEntity: EventEntity?,
            val checkoutModel = CheckoutModel ()
            checkoutModel.checkout_uid = baseCheckout.checkout_uid
            checkoutModel.salon_key  = baseCheckout.salon_key
            checkoutModel.event_key  = baseCheckout.event_key
            checkoutModel.price   = baseCheckout.price
            checkoutModel.is_manual_price     = baseCheckout.is_manual_price
            checkoutModel.reserve_fund     = baseCheckout.reserve_fund
            checkoutModel.paid_fund     = baseCheckout.paid_fund
            checkoutModel.author_employee_key     = baseCheckout.author_employee_key
            checkoutModel.paid_types     = baseCheckout.paid_types
            checkoutModel.created_at = baseCheckout.created_at
            checkoutModel.updated_at = baseCheckout.updated_at
            checkoutModel.reserveFund   = baseCheckout.reserveFund
            checkoutModel.paidFund  = baseCheckout.paidFund
            checkoutModel.tip  = baseCheckout.tip

            return checkoutModel
        }

            fun transformFromEntity(checkoutEntity: CheckoutEntity): CheckoutModel {
                val checkoutModel = CheckoutModel()
                checkoutModel.checkout_uid = checkoutEntity.checkout_uid
                checkoutModel.salon_key  = checkoutEntity.salon_key
                checkoutModel.event_key  = checkoutEntity.event_key
                checkoutModel.price   = checkoutEntity.price
                checkoutModel.is_manual_price     = checkoutEntity.is_manual_price
                checkoutModel.reserve_fund     = checkoutEntity.reserve_fund
                checkoutModel.paid_fund     = checkoutEntity.paid_fund
                checkoutModel.author_employee_key     = checkoutEntity.author_employee_key
                checkoutModel.paid_types     = checkoutEntity.paid_types
                checkoutModel.created_at = checkoutEntity.created_at
                checkoutModel.updated_at = checkoutEntity.updated_at
                checkoutModel.reserveFund   = checkoutEntity.reserveFund
                checkoutModel.paidFund  = checkoutEntity.paidFund
                checkoutModel.tip  = checkoutEntity.tip

                return checkoutModel
            }


    }

}