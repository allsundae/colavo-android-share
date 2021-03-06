package com.colavo.android.net

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.MemoEntity
import com.colavo.android.entity.checkout.PaidoutEntity
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.salon.SalonEntity
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.session.User
import retrofit2.http.*
import rx.Observable

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


    @GET("salon_customers/{salonid}/{id}.json")
    fun getCustomerBySalonCustomerId(@Path("salonid") salon_key: String, @Path("id") customer_key: String): Observable<CustomerEntity>

    @GET("memos/{id}.json")
    fun getMemoByMemoId(@Path("id") memo_key: String ): Observable<MemoEntity?>

    @GET("salon_checkouts/{salonid}/{id}.json")
    fun getPaidoutBySalonCheckoutId(@Path("salonid") salon_key: String, @Path("id") checkout_key: String): Observable<PaidoutEntity>


    @GET("salon_customers/{id}.json") //todo
    fun getCustomerbySalonKey(@Path("id") salon_key: String): Observable<CustomerEntity>

    @POST("salon_customers/{salonid}.json")
    fun createCustomer(@Path("salonid") salon_key: String, @Body customerEntity: CustomerEntity): Observable<FirebaseResponse>


    @GET("salon_checkouts/{id}.json")
    fun getCheckoutBySalonId(@Path("id") salon_key: String): Observable<CheckoutEntity>

    @GET("salon_checkouts/{id}.json")
    fun createCheckout(@Path("id") id: String, @Body checkoutEntity: CheckoutEntity): Observable<FirebaseResponse>

}