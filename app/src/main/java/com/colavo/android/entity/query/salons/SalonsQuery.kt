package com.colavo.android.entity.query.salons

import com.colavo.android.entity.query.BaseQuery

/**
 * Created by RUS on 19.07.2016.
 */
sealed class SalonsQuery : BaseQuery {
    class GetSalons(val ownerUid: String) : SalonsQuery()
    class CreateSalon(val salonName: String, val salonAddress: String, val ownerUid: String) : SalonsQuery()
}