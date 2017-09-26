package com.colavo.android.presenters.salons

import com.colavo.android.presenters.BasePresenter
import com.colavo.android.ui.salons.SalonlistView

/**
 * Created by RUS on 17.07.2016.
 */
interface SalonsPresenter : BasePresenter {

    fun attachView(salonlistView: SalonlistView)

    fun initialize(ownerId: String)

    fun onCreateSalonButtonClicked()

    fun createSalon(salonName: String, salonAddress: String, ownerUid: String)

}