package com.colavo.android.net

import com.colavo.android.entity.checkout.CheckoutEntity
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



    @GET("salon_customers/{id}/{id2}.json")
    fun getCustomerBySalonCustomerId(@Path("id") salon_key: String, @Path("id2") customer_key: String): Observable<CustomerEntity>

    @GET("salon_customers/{id}.json")
    fun createCustomer(@Path("id") id: String, @Body customerEntity: CustomerEntity): Observable<FirebaseResponse>

    // Doing
    @GET("salon_events/-KxLewgRxUVdD-10r-9k/{id}.json")
    fun getEventbySalonEventKey( @Path("id" ) event_key: String): Observable<EventEntity>

    @GET("salon_customers/{id}.json") //todo
    fun getCustomerbySalonKey(@Path("id") salon_key: String): Observable<CustomerEntity>
//    fun getCustomerbySalonCustomerKey(@Path("salon") salon_key: String, @Path("customer") customer_key: String): Observable<CustomerEntity>

    @GET("test/{id}.json") //todo
    fun getCustomerbyCustomerKey(@Path("id") salon_customer_key: String): Observable<CustomerEntity>

    @GET("salon_checkouts/{id}.json")
    fun getCheckoutBySalonId(@Path("id") salon_key: String): Observable<CheckoutEntity>

    @GET("salon_checkouts/{id}.json")
    fun createCheckout(@Path("id") id: String, @Body checkoutEntity: CheckoutEntity): Observable<FirebaseResponse>

}