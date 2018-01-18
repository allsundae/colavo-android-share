package com.colavo.android.interactors.customer

import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.interactors.UseCase
import rx.Subscriber
import javax.inject.Inject

//TODO Customer
@UseCase
class CreateCustomer @Inject constructor(customerRepository: CustomerRepository) : CustomerUseCase(customerRepository) {

    fun execute(
            salonUid: String
            ,  customerName: String
            ,  customerPhone: String
            ,  customerImageUrls: ImageUrl
                , subscriber: Subscriber<FirebaseResponse>)
            = super.execute(CustomerQuery.CreateCustomer(
            salonUid, customerName,  customerPhone, customerImageUrls ), subscriber)

}