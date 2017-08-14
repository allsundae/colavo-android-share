package com.colavo.android.net

import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.salon.SalonEntity
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.session.User
import retrofit2.http.*
import rx.Observable

/**
 * Created by RUS on 17.07.2016.
 */
interface FirebaseAPI {

    @PUT("users/{id}.json")
    fun createUser(@Path("id") id: String, @Body user: User): Observable<User>

    @GET("users/{id}.json")
    fun getUserById(@Path("id") uid: String): Observable<User>

    @POST("salons.json")
    fun createSalon(@Body salonEntity: SalonEntity): Observable<FirebaseResponse>

    @POST("events.json")
    fun sendEvent(@Body eventEntity: EventEntity): Observable<FirebaseResponse>

    @GET("events/{id}.json")
    fun getEventById(@Path("id") id: String): Observable<EventEntity>

}