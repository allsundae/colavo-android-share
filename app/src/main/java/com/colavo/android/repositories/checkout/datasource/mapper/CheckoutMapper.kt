package com.colavo.android.repositories.checkout.datasource.mapper

import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.utils.Logger
import java.util.*
import com.colavo.android.R.id.textView
import com.colavo.android.entity.checkout.*
import com.colavo.android.utils.currencyFormatter
import java.text.NumberFormat


class CheckoutMapper {

    companion object {

        fun createCheckoutWithEventAndUser(baseCheckout: BaseCheckout, customer: CustomerEntity): CheckoutModel { //eventEntity: EventEntity?,
            val checkoutModel = CheckoutModel()
            checkoutModel.checkout_uid = baseCheckout.checkout_uid
            checkoutModel.salon_key = baseCheckout.salon_key
      /*      checkoutModel.event_key = baseCheckout.event_key
            checkoutModel.price = baseCheckout.price
            checkoutModel.is_manual_price = baseCheckout.is_manual_price
            checkoutModel.reserve_fund = baseCheckout.reserve_fund
            checkoutModel.paid_fund = baseCheckout.paid_fund
            checkoutModel.author_employee_key = baseCheckout.author_employee_key
            checkoutModel.paid_types = baseCheckout.paid_types
            checkoutModel.created_at = baseCheckout.created_at
            checkoutModel.updated_at = baseCheckout.updated_at
            checkoutModel.reserveFund = baseCheckout.reserveFund
            checkoutModel.paidFund = baseCheckout.paidFund
            checkoutModel.tip = baseCheckout.tip*/

            //checkoutModel.user_name = baseCheckout.
/*                    getCustomerKeybySalonEventKey(baseCheckout.salon_key, baseCheckout.event_key,
                            object : SimpleCallback<String> {
                                override fun callback(data: String) {
                                    checkoutModel.user_name = data
                                    Logger.log("CHECKOUT added : callback data: ${data}, customer_key :${checkout.customer_key}")
                                }
                            }
                    )*/
            /*if (customer != null) {
                if (customer.name != null)
                    checkoutModel.user_name = "TEST"customer.name //todo address lastEventUser

                if (customer.image_urls.thumb != "")
                    checkoutModel.user_image = customer.image_urls.thumb
            }*/
            Logger.log("CHECKOUTMAPPER : createCheckoutWithEventAndUser : ${checkoutModel.checkout_uid}")

            return checkoutModel
        }

            fun transformFromEntity(checkoutEntity: CheckoutEntity, customerEntity: CustomerEntity, paidoutEntity: PaidoutEntity, memoEntity: MemoEntity ): CheckoutModel { //, paidoutEntity: PaidoutEntity, memoEntity: MemoEntity
                Logger.log("(4) CHECKOUTMAPPER ")
                val checkoutModel = CheckoutModel()
                checkoutModel.checkout_key = checkoutEntity.checkout_key
                checkoutModel.created_at = checkoutEntity.created_at
                checkoutModel.updated_at = checkoutEntity.updated_at
                checkoutModel.begin_at = checkoutEntity.begin_at
                checkoutModel.end_at = checkoutEntity.end_at
                checkoutModel.employee_only_event_title = checkoutEntity.employee_only_event_title
                checkoutModel.booked_by_customer = checkoutEntity.booked_by_customer
                checkoutModel.salon_key = checkoutEntity.salon_key
                checkoutModel.employee_key = checkoutEntity.employee_key
                checkoutModel.customer_key = checkoutEntity.customer_key
                checkoutModel.memo_key = checkoutEntity.memo_key
                checkoutModel.checkout_key = checkoutEntity.checkout_key
                checkoutModel.cancel_reason = checkoutEntity.cancel_reason
                //checkoutModel.services = checkoutEntity.services.toMutableList()
                checkoutModel.services = HashMap(checkoutEntity.services)
                checkoutModel.discounts = HashMap(checkoutEntity.discounts)
                checkoutModel.logs = HashMap(checkoutEntity.logs)

                checkoutModel.customer_name = customerEntity.name
                checkoutModel.service_menus = ""
                checkoutModel.customer_image_full = customerEntity.image_urls.full
                checkoutModel.customer_image_thumb = customerEntity.image_urls.thumb
                checkoutModel.customer_phone = customerEntity.phone
                checkoutModel.customer_fund = customerEntity.fund


                if (checkoutEntity.memo_key != "") {
                    checkoutModel.memo_txt = memoEntity.txt
                }
/*                else{
                    checkoutModel.memo_txt = new_memo
                }*/

                val cur = currencyFormatter(paidoutEntity.price)
                checkoutModel.checkout_price = cur.toString()

                for ((key, value) in paidoutEntity.paid_types) {
                    checkoutModel.checkout_paid_type = key.toString()
                }

                /*val numOfItems = checkoutModel.services.size
                Logger.log("(4) CHECKOUTMAPPER : numOfItems : ${numOfItems} ")*/

                var i = 0
                for ((key, value) in checkoutModel.services) {
                    println("$key -> $value")
                    if (i == 0)
                        checkoutModel.service_menus += value.name.toString()
                    else
                        checkoutModel.service_menus = checkoutModel.service_menus + ", " + value.name.toString()
                    i = i+1
                }

                Logger.log("(4) CHECKOUTMAPPER : transformFromEntity : name : ${checkoutModel.customer_name} : ${checkoutModel.checkout_uid}")


                return checkoutModel
            }




    }

}