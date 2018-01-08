package com.colavo.android.repositories.customerdetail.datasource.mapper

import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customerdetail.BaseCustomerDetail
import com.colavo.android.entity.customerdetail.CustomerDetailEntity
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.utils.Logger
import com.colavo.android.utils.SimpleCallback
import kotlinx.android.synthetic.main.customer_detail_fragment.*


class CustomerDetailMapper {

    companion object {

        fun createCustomerDetailWithEventAndUser(baseCustomerDetail: BaseCustomerDetail, customer: CustomerEntity): CustomerDetailModel { //eventEntity: EventEntity?,
            val customerDetailModel = CustomerDetailModel()
            customerDetailModel.id = baseCustomerDetail.id
            customerDetailModel.created_at = baseCustomerDetail.created_at
            customerDetailModel.updated_at = baseCustomerDetail.updated_at
            customerDetailModel.begin_at = baseCustomerDetail.begin_at
            customerDetailModel.end_at = baseCustomerDetail.end_at
            customerDetailModel.employee_only_event_title = baseCustomerDetail.employee_only_event_title
            customerDetailModel.booked_by_customer = baseCustomerDetail.booked_by_customer
            customerDetailModel.salon_key = baseCustomerDetail.salon_key
            customerDetailModel.employee_key = baseCustomerDetail.employee_key
            customerDetailModel.customer_key = baseCustomerDetail.customer_key
            customerDetailModel.memo_key = baseCustomerDetail.memo_key
            customerDetailModel.checkout_key = baseCustomerDetail.checkout_key
            customerDetailModel.cancel_reason = baseCustomerDetail.cancel_reason

            Logger.log("CUSTOMERDETAILMAPPER : createCustomerDetailWithEventAndUser : ${customerDetailModel.id}")

            return customerDetailModel
        }

       /*     fun transformFromEntity(customerDetailEntity: CustomerDetailEntity): CustomerDetailModel { //,  customerEntity: CustomerEntity
                val customerDetailModel = CustomerDetailModel()
                customerDetailModel.id = customerDetailEntity.id
                customerDetailModel.created_at = customerDetailEntity.created_at
                customerDetailModel.updated_at = customerDetailEntity.updated_at
                customerDetailModel.begin_at = customerDetailEntity.begin_at
                customerDetailModel.end_at = customerDetailEntity.end_at
                customerDetailModel.employee_only_event_title = customerDetailEntity.employee_only_event_title
                customerDetailModel.booked_by_customer = customerDetailEntity.booked_by_customer
                customerDetailModel.salon_key = customerDetailEntity.salon_key
                customerDetailModel.employee_key = customerDetailEntity.employee_key
                customerDetailModel.customer_key = customerDetailEntity.customer_key
                customerDetailModel.memo_key = customerDetailEntity.memo_key
                customerDetailModel.checkout_key = customerDetailEntity.checkout_key
                customerDetailModel.cancel_reason = customerDetailEntity.cancel_reason
                customerDetailModel.services = customerDetailEntity.services
                customerDetailModel.discounts = customerDetailEntity.discounts
                customerDetailModel.logs = customerDetailEntity.logs
                customerDetailModel.customer_name = customerDetailEntity.customer_name
                customerDetailModel.customer_image_full_url = customerDetailEntity.customer_image_full_url
                customerDetailModel.customer_image_thumb_url = customerDetailEntity.customer_image_thumb_url
                customerDetailModel.customer_menu = customerDetailEntity.customer_menu
                customerDetailModel.memo = customerDetailEntity.memo
                Logger.log("(4) CUSTOMERDETAILMAPPER : transformFromEntity : name : ${customerDetailEntity.customer_name} : ${customerDetailModel.id}")

                return customerDetailModel
            }*/


    }

}