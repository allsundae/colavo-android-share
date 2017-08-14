package com.colavo.android.di.event

import com.colavo.android.ui.event.eventActivity
import dagger.Subcomponent

/**
 * Created by RUS on 23.07.2016.
 */
@Subcomponent(modules = arrayOf(EventModule::class))
@EventScope
interface EventComponent {

    fun inject(eventActivity: eventActivity)

}