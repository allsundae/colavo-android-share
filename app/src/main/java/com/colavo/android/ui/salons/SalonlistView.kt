package com.colavo.android.ui.salons

import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.base.BaseView


interface SalonlistView : BaseView {

    fun setSalonlist(salonEntities: List<SalonModel>?)

    fun showCreateSalonlistFragment()

    fun addSalon(salonEntity: SalonModel)

    fun changeSalon(salonEntity: SalonModel)

    fun removeSalon(salonEntity: SalonModel)

    fun openEventActivity(salonModel: SalonModel)

    fun showProgress()

    fun hideProgress()

}