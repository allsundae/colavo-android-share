package com.colavo.android.interactors.customer

import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.entity.response.FirebaseResponse
import rx.Subscriber
import javax.inject.Inject

//TODO Customer
class CreateCustomer @Inject constructor(customerRepository: CustomerRepository) : CustomerUseCase(customerRepository) {

    fun execute(salonUid: String, customerUid: String, customerPhone: String,  customerName: String,  customerImageUrl: String
                , subscriber: Subscriber<FirebaseResponse>)
            = super.execute(CustomerQuery.CreateCustomer(salonUid, customerUid, customerPhone, customerName, customerImageUrl ), subscriber)

}