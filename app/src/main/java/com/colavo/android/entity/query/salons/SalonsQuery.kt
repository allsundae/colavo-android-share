package com.colavo.android.entity.query.salons

import com.colavo.android.entity.query.BaseQuery

/**
 * Created by RUS on 19.07.2016.
 */
sealed class SalonsQuery : BaseQuery {
    class GetSalons : SalonsQuery()
    class CreateSalon(val salonName: String) : SalonsQuery()
}