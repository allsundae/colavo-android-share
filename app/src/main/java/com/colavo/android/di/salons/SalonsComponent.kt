package com.colavo.android.di.salons

import com.colavo.android.ui.salons.SalonListActivity
import dagger.Subcomponent

/**
 * Created by RUS on 20.07.2016.
 */
@Subcomponent(modules = arrayOf(SalonsModule::class))
@SalonsScope
interface SalonsComponent {

    fun inject(salonListActivity: SalonListActivity)

}