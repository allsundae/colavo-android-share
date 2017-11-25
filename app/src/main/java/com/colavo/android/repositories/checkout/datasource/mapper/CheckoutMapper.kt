package com.colavo.android.repositories.checkout.datasource.mapper

import com.colavo.android.entity.checkout.BaseCheckout
import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.session.User
import com.colavo.android.utils.Logger
import com.colavo.android.utils.SimpleCallback


class CheckoutMapper {

    companion object {

        fun createCheckoutWithEventAndUser(baseCheckout: BaseCheckout, customer: CustomerEntity): CheckoutModel { //eventEntity: EventEntity?,
            val checkoutModel = CheckoutModel()
            checkoutModel.checkout_uid = baseCheckout.checkout_uid
            checkoutModel.salon_key = baseCheckout.salon_key
            checkoutModel.event_key = baseCheckout.event_key
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
            checkoutModel.tip = baseCheckout.tip

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

                if (customer.image_urls[0].image_thumb_url != "")
                    checkoutModel.user_image = customer.image_urls[0].image_thumb_url
            }*/
            Logger.log("CHECKOUTMAPPER : createCheckoutWithEventAndUser : ${checkoutModel.checkout_uid}")

            return checkoutModel
        }

            fun transformFromEntity(checkoutEntity: CheckoutEntity ,  customerEntity: CustomerEntity): CheckoutModel { //,  customerEntity: CustomerEntity
                val checkoutModel = CheckoutModel()
                checkoutModel.checkout_uid = checkoutEntity.checkout_uid
                checkoutModel.salon_key = checkoutEntity.salon_key
                checkoutModel.event_key = checkoutEntity.event_key
                checkoutModel.price = checkoutEntity.price
                checkoutModel.is_manual_price = checkoutEntity.is_manual_price
                checkoutModel.reserve_fund = checkoutEntity.reserve_fund
                checkoutModel.paid_fund = checkoutEntity.paid_fund
                checkoutModel.author_employee_key = checkoutEntity.author_employee_key
                checkoutModel.paid_types = checkoutEntity.paid_types
                checkoutModel.created_at = checkoutEntity.created_at
                checkoutModel.updated_at = checkoutEntity.updated_at
                checkoutModel.reserveFund = checkoutEntity.reserveFund
                checkoutModel.paidFund = checkoutEntity.paidFund
                checkoutModel.tip = checkoutEntity.tip
                checkoutModel.customer_key = checkoutEntity.customer_key



/*

               if (customerEntity != null){
                    checkoutModel.user_name = customerEntity.name
                    checkoutModel.user_image = customerEntity.image_urls[0].image_thumb_url
                   Logger.log("CHECKOUTMAPPER : customerEntity.name : ${customerEntity.name}")
                }
                else {
                    checkoutModel.user_name = "UNKNOWNPLAYER" //customerEntity.name
                    checkoutModel.user_image = "https://firebasestorage.googleapis.com/v0/b/colavo-ae9bd.appspot.com/o/images%2Fcustomers%2F-KusC2p08Hh4w8DqdAkc%2Fprofiles%2Fprofile_thumb.png?alt=media&token=b674e47d-59de-467c-8cb8-52b59febf12e"
                }
*/


                checkoutModel.user_name = "UNKNOWNPLAYER" //customerEntity.name
                checkoutModel.user_image = "https://firebasestorage.googleapis.com/v0/b/colavo-ae9bd.appspot.com/o/images%2Fcustomers%2F-KusC-aZyWqiAP_LmHk7%2Fprofiles%2Fprofile_thumb.png?alt=media&token=37904776-f8a1-4f43-937d-edb25466b5b9"


                checkoutModel.user_menu = "Menu 1, Menu 2"


/*                if (customer != null) {
                    if (customer.name != null)
                        checkoutModel.user_name = customer.name //todo address lastEventUser

                    if (customer.image_urls[0].image_thumb_url != "")
                        checkoutModel.user_image = customer.image_urls[0].image_thumb_url
                }*/
                Logger.log("CHECKOUTMAPPER : transformFromEntity : ${checkoutModel.checkout_uid}")

                return checkoutModel
            }


    }

}