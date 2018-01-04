package com.colavo.android.repositories.customer.datasource.mapper

import com.colavo.android.entity.customer.BaseCustomer
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.customer.ImageUrl


class CustomerMapper {

    companion object {

        fun createCustomerWithEventAndUser(baseCustomer: BaseCustomer, customer: CustomerEntity?): CustomerModel { //eventEntity: EventEntity?,
            val customerModel = CustomerModel ()
            customerModel.uid = baseCustomer.uid
            customerModel.phone = baseCustomer.phone
            customerModel.national_phone = baseCustomer.national_phone
            customerModel.name = baseCustomer.name
            customerModel.image_urls.image_full_url = baseCustomer.image_urls.image_full_url
            customerModel.image_urls.image_thumb_url = baseCustomer.image_urls.image_thumb_url
            customerModel.recent_appointment_begin_at = baseCustomer.recent_appointment_begin_at
            customerModel.recent_appointment_end_at = baseCustomer.recent_appointment_end_at
            customerModel.is_removed = baseCustomer.is_removed
            customerModel.fund = baseCustomer.fund
//todo
/*
            if (user != null) {
                if (user.name != null)
                    customerModel.owner = user.name //todo address lastEventUser
            }*/
            return customerModel
        }

            fun transformFromEntity(customerEntity: CustomerEntity): CustomerModel {
                val customerModel = CustomerModel()
                customerModel.uid = customerEntity.uid
                customerModel.phone = customerEntity.phone
                customerModel.national_phone = customerEntity.national_phone
                customerModel.name = customerEntity.name
                customerModel.image_urls.image_full_url = customerEntity.image_urls.image_full_url// customerEntity.image_urls!!.image_full_url
                customerModel.image_urls.image_thumb_url = customerEntity.image_urls.image_thumb_url
                customerModel.recent_appointment_begin_at = customerEntity.recent_appointment_begin_at
                customerModel.recent_appointment_end_at = customerEntity.recent_appointment_end_at
                customerModel.is_removed = customerEntity.is_removed
                customerModel.fund = customerEntity.fund

                return customerModel
            }


    }

}