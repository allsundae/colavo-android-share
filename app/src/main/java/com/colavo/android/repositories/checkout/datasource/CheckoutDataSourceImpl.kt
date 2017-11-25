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
import com.google.firebase.database.DatabaseReference
import android.support.annotation.NonNull
import com.colavo.android.utils.SimpleCallback

class CheckoutDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CheckoutDataSource {

    override fun initialize(query: CheckoutQuery.GetCheckout): Observable<Pair<CheckoutModel, ResponseType>>
            = Observable.create<Pair<CheckoutEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("salon_checkouts")
                        .child(query.salonUid)
                        .orderByChild("author_employee_key").equalTo(getCurrentFirebaseUserKey())
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
                                        Logger.log("CHECKOUT ADDED : event_key : ${checkout.event_key}")
/*
                                        getCustomerKeybySalonEventKey(checkout.salon_key, checkout.event_key, object : SimpleCallback<String> {
                                            override fun callback(data: String) {
                                                checkout.customer_key = data
                                                Logger.log("CHECKOUT added : callback data: ${data} -> customer_key :${checkout.customer_key}")
                                            }
                                        })
*/
                                        val firebaseDatabase2: FirebaseDatabase = FirebaseDatabase.getInstance()
                                        firebaseDatabase2.reference.child("salon_events")
                                                //    .child(checkout.author_employee_key)
                                                .child(checkout.salon_key)
                                                .child(checkout.event_key)
                                                .child("customer_key")
                                                .addListenerForSingleValueEvent( object :ValueEventListener {

                                                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                                                        val customer_key : String = dataSnapshot2.getValue(String::class.java)
                                                      //  customerCallback.callback(customer_key)
                                                        checkout.customer_key = customer_key
                                                        Logger.log("CHECKOUT ADDED : customer_key: ${customer_key} -> ${checkout.customer_key}")
                                                        subscriber.onNext(checkout to ResponseType.ADDED)
                                                    }

                                                    override fun onCancelled(databaseError: DatabaseError) {
                                                        val customer_Key = "CUSTOMER_KEY READ FAILED: " + databaseError.code
                                                        Logger.log("getCustomerKeybySalonEventKey : CUSTOMER_KEY READ FAILED: ${databaseError.code.toString()}")
                                                    }
                                                })

                                       // subscriber.onNext(checkout to ResponseType.ADDED)
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
            /*. DEVELOPING */
            .concatMap { response -> convertToCheckoutModel(response)}
/* TODO This is WORKING on GITHUB */
/*            .concatMapEager { pair -> Observable.zip(Observable.just(pair), getCheckoutbySalonKey(pair.first.salon_key) , getCustomerbyCustomerKey(pair.first.customer_key)//getCheckoutbySalonKey(pair.first.salon_key) //, getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)//getEventbySalonEventKey(pair.first.salon_key, pair.first.event_key)
                                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                        , { pair, idontknow, customer -> CheckoutMapper.transformFromEntity(pair.first, customer) to pair.second }
                                        )
                            }
*/

    private fun convertToCheckoutModel(pair: Pair<CheckoutEntity, ResponseType>) : Observable< Pair<CheckoutModel, ResponseType>> {
        Logger.log("convertToCheckoutModel : checkout_uid: ${pair.first.checkout_uid}, customer_key: ${pair.first.customer_key}")

        var customer : CustomerEntity? = null

        getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key, object : SimpleCallback<CustomerEntity>{
            override fun callback(customerCallback: CustomerEntity){
                customer = customerCallback
                Logger.log("getCustomerbySalonCustomerKey : Callback : ${customerCallback.name} -> var customer : ${customer!!.name}")
            }
        })

        return Observable.zip(Observable.just(pair.first), Observable.just(customer!!))
                {WTF, customer -> CheckoutMapper.transformFromEntity(pair.first, customer) to pair.second}
        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

/*        return getCheckoutbySalonKey(pair.first.salon_key)
                .concatMapEager { test ->
                    Observable.zip(Observable.just(test), getCheckoutbySalonKey(pair.first.salon_key))
                    { WTF, arigato -> CheckoutMapper.transformFromEntity(pair.first, customer!!) to pair.second }
                } .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/

    }
/* DEVELOPING
        return event //getEventbySalonEventKey(pair.first.salon_key, pair.first.event_key)
                .concatMapEager { checkouts -> Observable.zip(Observable.just(checkouts), getCustomerbySalonCustomerKey(pair.first.salon_key, checkouts.customer_key))
                            { checkouts, user -> CheckoutMapper.createCheckoutWithEventAndUser(pair.first, user) to pair.second }
                            }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/
/*
        return getCustomerbySalonCustomerKey(pair.first.salon_key, "-KusC-aZyWqiAP_LmHk7")
                    .concatMapEager { checkouts -> Observable.zip(Observable.just(checkouts), getCustomerbySalonCustomerKey(pair.first.salon_key, "-KusC-aZyWqiAP_LmHk7"))
                                      { checkouts, customer -> CheckoutMapper.createCheckoutWithEventAndUser(pair.first, customer) to pair.second }
                                    }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
*/

    private fun getCustomerbySalonKey(salon_key: String?) : Observable<CustomerEntity>  {
        Logger.log("getCustomerbySalonKey: ${salon_key}")
        return retrofit.create(FirebaseAPI::class.java).getCustomerBySalonId(salon_key ?: "")
    }



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

    private fun getCheckoutbySalonKey(salon_key: String?)
            : Observable<CheckoutEntity> = retrofit.create(FirebaseAPI::class.java).getCheckoutBySalonId(salon_key?: "")

    private fun getEventbySalonEventKey(salon_key: String?, event_key: String?)
            : Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventbySalonEventKey(event_key ?: "")

    private fun getEventEntitybySalonEventKey(salon_key: String?, event_key: String?)
            : Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventbySalonEventKey(event_key ?: "")

    private fun getCustomerKeybySalonEventKey(salon_key: String?, event_key: String?, customerCallback: SimpleCallback<String>)
    {
        val firebaseDatabase2: FirebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase2.reference.child("salon_events")
            //    .child(checkout.author_employee_key)
                .child(salon_key)
                .child(event_key)
                .addListenerForSingleValueEvent( object :ValueEventListener {

                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                        val customer_key : String = dataSnapshot2.getValue(String::class.java)
                        customerCallback.callback(customer_key)

                        Logger.log("getCustomerKeybySalonEventKey : customer_key: ${customer_key}")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val customer_Key = "CUSTOMER_KEY READ FAILED: " + databaseError.code
                        Logger.log("getCustomerKeybySalonEventKey : CUSTOMER_KEY READ FAILED: ${databaseError.code.toString()}")
                    }
                })

/*        firebaseDatabase.reference.child("salon_events")
                .child(salon_key)
                .child(event_key)
                .child("customer_key")
                .addListenerForSingleValueEvent( object :ValueEventListener {

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val customer_key : String = dataSnapshot.getValue(String::class.java)
                            finishedCallback.callback(customer_key)
                            Logger.log("getCustomerKeybySalonEventKey : ${customer_key}")
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            val customer_Key = "CUSTOMER_KEY READ FAILED: " + databaseError.code
                            Logger.log("getCustomerKeybySalonEventKey : CUSTOMER_KEY READ FAILED: ${databaseError.code.toString()}")
                        }
                })*/

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
                        finishedCallback.callback(customer);
                        Logger.log("getCustomerbySalonCustomerKey : customer_name: ${customer.name}")
                        //subscriber.onNext(customer to ResponseType.ADDED)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Logger.log("getCustomerbySalonCustomerKey : FAILED: ${databaseError.code.toString()}")
                    }
                })

    }

    /*
       private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?)
               : Observable<CustomerEntity> //= retrofit.create(FirebaseAPI::class.java).getCustomerbySalonCustomerKey(salon_key ?: "", customer_key ?: "")

       {
           val salon_customer_key="${salon_key}/${customer_key}"
           Logger.log("getCustomerbySalonCustomerKey: ${salon_customer_key}")
   //        Logger.log("getCustomerbySalonCustomerKey: ${salon_customer_key} : ${customer_key}")
           return  retrofit.create(FirebaseAPI::class.java).getCustomerbySalonCustomerKey(salon_customer_key ?:"")
       }
   */


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

 //   private fun getEventbyId(eventId: String?): Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventById(eventId ?: "")

}