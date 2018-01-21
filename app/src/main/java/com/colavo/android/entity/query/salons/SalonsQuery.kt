package com.colavo.android.entity.query.salons

import com.colavo.android.entity.query.BaseQuery

sealed class SalonsQuery : BaseQuery {
    class GetSalons(val ownerUid: String) : SalonsQuery()
    class CreateSalon(val salonName: String, val salonAddress: String, val ownerUid: String) : SalonsQuery()
}