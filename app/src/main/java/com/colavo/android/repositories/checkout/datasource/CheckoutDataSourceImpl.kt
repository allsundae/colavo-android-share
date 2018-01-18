package com.colavo.android.repositories.checkout.datasource

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.checkout.MemoEntity
import com.colavo.android.entity.checkout.PaidoutEntity
import com.colavo.android.entity.customer.CustomerEntity
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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase






class CheckoutDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CheckoutDataSource {

    override fun initialize(query: CheckoutQuery.GetCheckout): Observable<Pair<CheckoutModel, ResponseType>>
            = Observable.create<Pair<CheckoutEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("employee_events")
                        //.child(query.salonUid)
                        .child(getCurrentFirebaseUserKey())
                        .orderByChild("salon_key").equalTo(query.salonUid)
                        //.orderByChild("begin_at").equalTo(query.salonUid)
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout?.checkout_uid = dataSnapshot.key
                                        Logger.log("CHECKOUT changed ${checkout?.checkout_uid}")
                                        subscriber.onNext(checkout!! to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout?.checkout_uid = dataSnapshot.key
                                        Logger.log("(1) CHECKOUT ADDED : checkout_uid : ${checkout?.checkout_uid}")

                                        // 1. get Customer
                                        subscriber.onNext(checkout!! to ResponseType.ADDED)
                                    }

                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout?.checkout_uid = dataSnapshot.key
                                        subscriber.onNext(checkout!! to ResponseType.REMOVED)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError?) {
                                    Logger.log("(1) CHECKOUT ERROR : ${error.toString()}")
                                    subscriber.onError(error?.toException())
                                }

                        })
            }
/* TODO This is WORKING on GITHUB */
            .concatMapEager { pair -> Observable.zip(
                    Observable.just(pair)
                    ,getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)
                    ,getPaidoutbySalonCheckoutKey(pair.first.salon_key, pair.first.checkout_key)
                    ,getMemobyMemoKey(pair.first.memo_key)
                    , { pair, customer, paidout,  memo -> CheckoutMapper.transformFromEntity(pair.first, customer, paidout!!, memo!!) to pair.second }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }


    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?) : Observable<CustomerEntity>  {
        Logger.log("(2) getCustomerbySalonCustomerKey: $salon_key, $customer_key")
        return retrofit.create(FirebaseAPI::class.java).getCustomerBySalonCustomerId(salon_key ?: "", customer_key ?: "")
    }

    private fun getMemobyMemoKey(memo_key: String?) : Observable<MemoEntity?>  {

        if (memo_key != null && memo_key != ""){
            Logger.log("(2-2) getMemobyMemoKey: $memo_key")
            return retrofit.create(FirebaseAPI::class.java).getMemoByMemoId(memo_key ?: "" )
        }
        else{
            Logger.log("(2-2) getMemobyMemoKey: NULL")
            val empty = MemoEntity("", hashMapOf(),0.0,0.0,"","","","")
            return Observable.just(empty)
        }
    }

    private fun getPaidoutbySalonCheckoutKey(salon_key: String?, checkout_key: String?) : Observable<PaidoutEntity>  {
        if (checkout_key != null && checkout_key != "") {
            Logger.log("(2-1) getPaidoutbySalonCheckoutKey: ${salon_key}, ${checkout_key}")
            return retrofit.create(FirebaseAPI::class.java).getPaidoutBySalonCheckoutId(salon_key ?: "" , checkout_key ?: "") //"KtA1nZ5MFIYgIoeJ3YQ" salon_key ?: ""
        }
        else {
            Logger.log("(2-1) getPaidoutbySalonCheckoutKey: NULL")
            val empty = PaidoutEntity(0.0, 0.0, 0.0, false
                    , 0.0, 0.0, hashMapOf(),0.0
            ,"", 0.0, 0.0)
            return Observable.just(empty)
        }
    }

    override fun createCheckout(query: CheckoutQuery.CreateCheckout): Observable<FirebaseResponse>
    {
        return   retrofit.create(FirebaseAPI::class.java)
                .createCheckout(query.salon_key
                        , CheckoutEntity(checkout_uid = query.checkout_uid
                        ,salon_key = query.salon_key
/*                        ,event_key = query.event_key
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
                        */
                ))

    }


    private fun getCurrentFirebaseUserKey() : String {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        Logger.log("current login user UID ${currentFirebaseUser!!.uid}")
        return currentFirebaseUser!!.uid
    }


}