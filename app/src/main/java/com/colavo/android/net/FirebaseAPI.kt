package com.colavo.android.net

import com.colavo.android.entity.customer.CustomerEntity
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

    @GET("users/{id}.json") //todo
    fun getUserById(@Path("id") uid: String): Observable<User>

    @POST("salons.json")
    fun createSalon(@Body salonEntity: SalonEntity): Observable<FirebaseResponse>

    @POST("salon_events/{id}.json")
    fun sendEvent(@Path("id") id: String, @Body eventEntity: EventEntity): Observable<FirebaseResponse>

    @GET("salon_events/{id}.json")
    fun getEventById(@Path("id") id: String): Observable<EventEntity>

    @GET("salon_customers/{id}.json")
    fun getCustomerBySalonId(@Path("id") uid: String): Observable<CustomerEntity>

    @GET("salon_customers/{id}.json")
    fun createCustomer(@Path("id") id: String, @Body customerEntity: CustomerEntity): Observable<FirebaseResponse>

}