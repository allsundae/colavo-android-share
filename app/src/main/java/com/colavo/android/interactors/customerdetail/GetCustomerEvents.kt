package com.colavo.android.interactors.customerdetail

import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.repositories.customerdetail.CustomerDetailRepository
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.interactors.UseCase
import rx.Subscriber
import javax.inject.Inject

@UseCase
class GetCustomerEvents @Inject constructor(customerDetailRepository: CustomerDetailRepository) : CustomerDetailUseCase(customerDetailRepository) {

    fun execute(customerUid:String, subscriber: Subscriber<Pair<CustomerDetailModel, ResponseType>>)
            = super.execute(CustomerDetailQuery.GetCustomerEvents(customerUid), subscriber)

}