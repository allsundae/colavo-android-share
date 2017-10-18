package com.colavo.android.interactors.customer

import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.customer.CustomerRepository
import com.colavo.android.repositories.salons.SalonsRepository
import rx.Subscriber
import javax.inject.Inject


@UseCase
class GetSalonCustomer @Inject constructor(customerRepository: CustomerRepository) : CustomerUseCase(customerRepository) {

    fun execute(salonUid:String, subscriber: Subscriber<Pair<CustomerModel, ResponseType>>)
            = super.execute(CustomerQuery.GetCustomer(salonUid), subscriber)

}