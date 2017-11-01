package com.colavo.android.entity.query.customer

import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.query.BaseQuery


sealed class CustomerQuery : BaseQuery {
    class GetCustomer(val salonUid: String) : CustomerQuery()
    class CreateCustomer(val salonUid: String, val customerUid: String
                         , val customerPhone: String, val customerName: String, val customerImageUrls: List<ImageUrl>) : CustomerQuery()
}