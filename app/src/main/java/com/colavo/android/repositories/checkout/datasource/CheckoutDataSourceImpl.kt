package com.colavo.android.repositories.checkout.datasource

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.customer.CustomerEntity
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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.utils.SimpleCallback


class CheckoutDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CheckoutDataSource {

    override fun initialize(query: CheckoutQuery.GetCheckout): Observable<Pair<CheckoutModel, ResponseType>>
            = Observable.create<Pair<CheckoutEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("employee_events")
                        //.child(query.salonUid)
                        .child(getCurrentFirebaseUserKey())
                        .orderByChild("begin_at")
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout.checkout_uid = dataSnapshot.key
                                        Logger.log("CHECKOUT changed ${checkout.checkout_uid}")
                                        subscriber.onNext(checkout to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val checkout = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        checkout.checkout_uid = dataSnapshot.key
                                        Logger.log("(1) CHECKOUT ADDED : checkout_uid : ${checkout.checkout_uid}")

                                        // 1. get Customer
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
                                    Logger.log("(1) CHECKOUT ERROR : ${error.toString()}")
                                    subscriber.onError(error?.toException())
                                }

                        })
            }
            /*. DEVELOPING */
//            .concatMap { response -> convertToCheckoutModel(response)}
/* TODO This is WORKING on GITHUB */
            .concatMapEager { pair -> Observable.zip(Observable.just(pair), getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    , { pair, customer -> CheckoutMapper.transformFromEntity(pair.first, customer) to pair.second }
            ) }
  /*          .flatMap { pair -> Observable.zip(Observable.just(pair), getUserById(pair.first.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()),
                    { pair, user -> MessageMapper.transformFromMessageEntityAndUser(pair.first, user, user.uid.equals(firebaseAuth.currentUser?.uid)) to pair.second } ) }
*/


    private fun convertToCheckoutModel(pair: Pair<CheckoutEntity, ResponseType>) : Observable< Pair<CheckoutModel, ResponseType>> {
        Logger.log("convertToCheckoutModel : checkout_uid: ${pair.first.checkout_uid}, customer_key: ${pair.first.customer_key}")

        return getCustomerbySalonCustomerKey((pair.first).salon_key, (pair.first).customer_key)
                .concatMapEager { test ->
                    Observable.zip(Observable.just(test), getCustomerbySalonCustomerKey((pair.first).salon_key, (pair.first).customer_key))
                    { WTF, customer -> CheckoutMapper.transformFromEntity(pair.first, customer) to pair.second }
                }.onBackpressureBuffer(10000).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }


    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?) : Observable<CustomerEntity>  {
        Logger.log("(2) getCustomerbySalonCustomerKey: ${salon_key}, ${customer_key}")

/*        val firebaseDatabase2: FirebaseDatabase = FirebaseDatabase.getInstance()
        return firebaseDatabase2.reference.child("salon_customers") // .child(checkout.author_employee_key)
                .child(salon_key)
                .child(customer_key)
                .addListenerForSingleValueEvent( object : ValueEventListener {
                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                        val customerEntity  = dataSnapshot2.getValue(CustomerEntity::class.java)
                        Logger.log("(2) CHECKOUT ADDED : customer: ${customerEntity.name}")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val customer_Key = "CUSTOMER_KEY READ FAILED: " + databaseError.code
                        Logger.log("getCustomerKeybySalonEventKey : CUSTOMER_KEY READ FAILED: ${databaseError.code.toString()}")
                    }
                })*/

        return retrofit.create(FirebaseAPI::class.java).getCustomerBySalonCustomerId(salon_key ?: "", customer_key ?: "")
     //   return retrofit.create(FirebaseAPI::class.java).getCustomerbySalonKey(salon_key ?: "")
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
                        ,tip = query.tip*/
                ))

    }

/*    private fun convertToCheckoutModel(pair: Pair<CheckoutEntity, ResponseType>)
            : Observable<Pair<CheckoutModel, ResponseType>> {
        return getCheckoutbySalonKey((pair.first).uid)
                .concatMap { checkouts -> Observable.zip(Observable.just(checkouts), getCheckoutbySalonKey(checkouts?.uid))
                              { checkouts, checkout -> CheckoutMapper.createCheckoutWithEventAndUser(pair.first, checkout) to pair.second }
                             }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/

    private fun getCheckoutbySalonKey(salon_key: String?)
            : Observable<CheckoutEntity> = retrofit.create(FirebaseAPI::class.java).getCheckoutBySalonId(salon_key?: "")

    private fun getEventbySalonEventKey(salon_key: String?, event_key: String?)
            : Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventbySalonEventKey(event_key ?: "")

    private fun getEventEntitybySalonEventKey(salon_key: String?, event_key: String?)
            : Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventbySalonEventKey(event_key ?: "")

    private fun getEventbySalonEventKey(salon_key: String?, event_key: String?, eventCallback: SimpleCallback<EventEntity>)
    {
        val firebaseDatabase2: FirebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase2.reference.child("salon_events")
                .child(salon_key)
                .child(event_key)
                .addListenerForSingleValueEvent( object :ValueEventListener {
                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                        val event : EventEntity = dataSnapshot2.getValue(EventEntity::class.java)
                        for (i in 0..event.services.size){
               //             event.services[i].key = dataSnapshot2.child("services").key[i]
               //             event.services[i].name = dataSnapshot2.child("services").child("")
                        }

                        Logger.log("getEventbySalonEventKey : customer_key: ${event.services.size}")
                        eventCallback.callback(event)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val customer_Key = "CUSTOMER_KEY READ FAILED: " + databaseError.code
                        Logger.log("getCustomerKeybySalonEventKey : CUSTOMER_KEY READ FAILED: ${databaseError.code.toString()}")
                    }
                })

    }

    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?, finishedCallback: SimpleCallback<CustomerEntity>)
    {
        val firebaseDatabase3: FirebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase3.reference.child("salon_customers")
                //    .child(checkout.author_employee_key)
                .child(salon_key)
                .child(customer_key)
                .addListenerForSingleValueEvent( object :ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val customer  = dataSnapshot.getValue(CustomerEntity::class.java)
                        customer.image_urls.thumb = (dataSnapshot.child("image_url").child("thumb").value).toString()
                        Logger.log("(2.5) getCustomerbySalonCustomerKey : customer_name: ${customer.name} (${customer.image_urls.thumb})")
                        finishedCallback.callback(customer)
                        //subscriber.onNext(customer to ResponseType.ADDED)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Logger.log("getCustomerbySalonCustomerKey : FAILED: ${databaseError.code.toString()}")
                    }
                })

    }


    private fun getCurrentFirebaseUserKey() : String {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        Logger.log("current login user UID ${currentFirebaseUser!!.uid}")
        return currentFirebaseUser!!.uid
    }


}