package com.colavo.android.ui.salons

import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.base.BaseView

/**
 * Created by RUS on 17.07.2016.
 */
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