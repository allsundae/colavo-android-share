package com.colavo.android.repositories.checkout.datasource

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.event.EventEntity
import com.google.firebase.database.*
import com.colavo.android.entity.query.checkout.CheckoutQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.net.FirebaseAPI
import com.colavo.android.repositories.checkout.datasource.mapper.CheckoutMapper
import com.colavo.android.utils.Logger
import retrofit2.Retrofit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser




class CheckoutDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CheckoutDataSource {

    override fun initialize(query: CheckoutQuery.GetCheckout): Observable<Pair<CheckoutModel, ResponseType>>
            = Observable.create<Pair<CheckoutEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("salon_checkouts")
                        .child(query.salonUid)
                        //.orderByChild("name").limitToFirst(200)
                           .orderByChild("author_employee_key").equalTo(getCurrentFirebaseUserKey())
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout.checkout_uid = dataSnapshot.key
                                        Logger.log("changed ${checkout.event_key}")
                                        subscriber.onNext(checkout to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout.checkout_uid = dataSnapshot.key
                                        Logger.log("added ${checkout.event_key}")
                                        subscriber.onNext(checkout to ResponseType.ADDED)
                                    }
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout.checkout_uid = dataSnapshot.key
                                        subscriber.onNext(checkout to ResponseType.REMOVED)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError?) {
                                    subscriber.onError(error?.toException())
                                }

                            })
            }
 /*           .flatMap { response -> convertToCheckoutModel(response)  }*/
                .flatMap { pair -> Observable.zip(Observable.just(pair)
                    , getCheckoutbySalonKey(pair.first.checkout_uid).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    , { pair, user -> CheckoutMapper.transformFromEntity(pair.first) to pair.second }
            ) }
/*
            .concatMap { pair -> Observable.zip(Observable.just(pair), getCheckoutbySalonKey(pair.first.uid))
                           { pair, user -> CheckoutMapper.transformFromEntity(pair.first) to pair.second }
                    }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
*/

    override fun createCheckout(query: CheckoutQuery.CreateCheckout): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createCheckout(query.salon_key
                            , CheckoutEntity(checkout_uid = query.checkout_uid
                                            ,salon_key = query.salon_key
                                            ,event_key = query.event_key
                                            ,price = query.price
                                            ,is_manual_price = query.is_manual_price
                                            ,reserve_fund = query.reserve_fund
                                            ,paid_fund = query.paid_fund
                                            ,author_employee_key = query.author_employee_key
                                            ,paid_types = query.paid_types
                                            ,created_at = query.created_at
                                            ,updated_at = query.updated_at
                                            ,reserveFund = query.reserveFund
                                            ,paidFund = query.paidFund
                                            ,tip = query.tip
                                                ))

/*    private fun convertToCheckoutModel(pair: Pair<CheckoutEntity, ResponseType>)
            : Observable<Pair<CheckoutModel, ResponseType>> {
        return getCheckoutbySalonKey((pair.first).uid)
                .concatMap { checkouts -> Observable.zip(Observable.just(checkouts), getCheckoutbySalonKey(checkouts?.uid))
                              { checkouts, checkout -> CheckoutMapper.createCheckoutWithEventAndUser(pair.first, checkout) to pair.second }
                             }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/


    private fun getCheckoutbySalonKey(uid: String?)
            : Observable<CheckoutEntity> = retrofit.create(FirebaseAPI::class.java).getCheckoutBySalonId(uid ?: "")

/*    private fun getNumberofCheckout : Int (){

        val numOfCheckouts: Int = datasnapshot.getChildrenCount()
        return val
    }
    */
    private fun getCurrentFirebaseUserKey() : String {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        Logger.log("current login user UID ${currentFirebaseUser!!.uid}")
        return currentFirebaseUser!!.uid
    }

    private fun getEventbyId(eventId: String?): Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventById(eventId ?: "")

}