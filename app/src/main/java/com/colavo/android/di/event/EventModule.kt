package com.colavo.android.di.event

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.event.GetSalonEvents
import com.colavo.android.interactors.event.SendEvent
import com.colavo.android.repositories.event.EventRepository
import com.colavo.android.repositories.event.datasource.EventDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by RUS on 23.07.2016.
 */
@Module
class EventModule {

    @Provides
    @EventScope
    fun getGetSalonEvents(eventRepository: EventRepository): GetSalonEvents = GetSalonEvents(eventRepository)

    @Provides
    @EventScope
    fun getSendEvent(eventRepository: EventRepository): SendEvent = SendEvent(eventRepository)

    @Provides
    @EventScope
    fun getEventRepository(eventDataSourceImpl: EventDataSourceImpl): EventRepository = EventRepository(eventDataSourceImpl)

    @Provides
    @EventScope
    fun getEventDataSourceImpl(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase, firebaseAuth: FirebaseAuth): EventDataSourceImpl
            = EventDataSourceImpl(retrofit, firebaseDatabase, firebaseAuth)

}