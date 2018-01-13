package com.colavo.android.repositories.customerdetail.datasource

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.checkout.MemoEntity
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customerdetail.CustomerDetailEntity
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.net.FirebaseAPI
import com.colavo.android.repositories.checkout.datasource.mapper.CheckoutMapper
import com.colavo.android.repositories.customerdetail.datasource.mapper.CustomerDetailMapper
import com.colavo.android.utils.Logger
import com.colavo.android.utils.SimpleCallback
import com.google.firebase.database.*
import retrofit2.Retrofit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.support.annotation.NonNull
import com.colavo.android.entity.checkout.PaidoutEntity


class CustomerDetailDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CustomerDetailDataSource {

    override fun initialize(query: CustomerDetailQuery.GetCustomerEvents): Observable<Pair<CheckoutModel, ResponseType>>
            = Observable.create<Pair<CheckoutEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("customer_events")
                        .child(query.customerUid)
                        .orderByChild("begin_at")//.equalTo(getCurrentFirebaseUserKey())
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        customerDetail?.checkout_uid = dataSnapshot.key
                                        Logger.log("CUSTOMERDETAIL changed ${customerDetail?.checkout_uid}")
                                        subscriber.onNext(customerDetail!! to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        customerDetail?.checkout_uid = dataSnapshot.key

                                        Logger.log("(1) CUSTOMERDETAIL ADDED : event_key : ${customerDetail?.checkout_uid} ")

                                        subscriber.onNext(customerDetail!! to ResponseType.ADDED)
                                    }
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CheckoutEntity::class.java)
                                        customerDetail?.checkout_uid = dataSnapshot.key
                                        subscriber.onNext(customerDetail!! to ResponseType.REMOVED)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError?) {
                                    Logger.log("(1) CUSTOMER ERROR : ${error.toString()}")
                                    subscriber.onError(error?.toException())
                                }

                        })
            }
/* TODO This is WORKING on GITHUB */
            .concatMapEager { pair -> Observable.zip(Observable.just(pair)
                                    , getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)
                                    ,getPaidoutbySalonCheckoutKey(pair.first.salon_key, pair.first.checkout_key)
                                    , getMemobyMemoKey(pair.first.memo_key)// /getCustomerDetailbySalonKey(pair.first.salon_key) //getCustomerDetailbySalonKey(pair.first.salon_key) //, getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)//getEventbySalonEventKey(pair.first.salon_key, pair.first.event_key)

                                        , { pair, customer, paidout,  memo -> CheckoutMapper.transformFromEntity(pair.first, customer, paidout,  memo!!) to pair.second }

            ).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())}




    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?) : Observable<CustomerEntity>  {
        Logger.log("(2) getCustomerbySalonCustomerKey: $salon_key, $customer_key")
        return retrofit.create(FirebaseAPI::class.java).getCustomerBySalonCustomerId(salon_key ?: "", customer_key ?: "")
    }

    private fun getMemobyMemoKey(memo_key: String?) : Observable<MemoEntity?>  {
//        Logger.log("(2-1) getMemobyMemoKey: $memo_key")
//        return retrofit.create(FirebaseAPI::class.java).getMemoByMemoId(memo_key ?: "")

/*        val client = OkHttpClient()
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 3
        client.dispatcher()*/

        if (memo_key != null && memo_key != ""){
            Logger.log("(2-2) getMemobyMemoKey: $memo_key")
            return retrofit.create(FirebaseAPI::class.java).getMemoByMemoId(memo_key ?: "" )
        }
        else{
            Logger.log("(2-2) getMemobyMemoKey: NULL")
            val empty = MemoEntity("", hashMapOf(),0.0,0.0,"","","","")
            return Observable.just(empty)
            //return retrofit.create(FirebaseAPI::class.java).getMemoByMemoId("-K_cG1-gLk_jJRK7cA7Q" )
            //return Observable.empty()//Observable.just()//Observable.empty()
        }
    }

    private fun getPaidoutbySalonCheckoutKey(salon_key: String?, checkout_key: String?) : Observable<PaidoutEntity>  {
        if (checkout_key != null && checkout_key != "") {
            Logger.log("(2-1) getPaidoutbySalonCheckoutKey: ${salon_key}, ${checkout_key}")
            return retrofit.create(FirebaseAPI::class.java).getPaidoutBySalonCheckoutId("KtA1nZ5MFIYgIoeJ3YQ", checkout_key ?: "")
        }
        else {
            Logger.log("(2-1) getPaidoutbySalonCheckoutKey: NULL")
            val empty = PaidoutEntity(0.0, 0.0, 0.0, false
                    , 0.0, 0.0, hashMapOf(),0.0
                    ,"", 0.0, 0.0)
            return Observable.just(empty)
            //return Observable.never()
        }
    }



    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?, finishedCallback: SimpleCallback<CustomerEntity>)
    {
        val firebaseDatabase3: FirebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase3.setPersistenceEnabled(true)
        firebaseDatabase3.reference.child("salon_customers")
                //    .child(customerDetail.author_employee_key)
                .child(salon_key)
                .child(customer_key)
                .addListenerForSingleValueEvent( object :ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val customer  = dataSnapshot.getValue(CustomerEntity::class.java)
 //                       customer.image_urls.thumb = (dataSnapshot.child("image_url").child("thumb").value).toString()
                        Logger.log("(2.5) getCustomerbySalonCustomerKey : customer_name: ${customer?.name} (${customer?.image_urls?.thumb})")
                        finishedCallback.callback(customer!!)
                        //subscriber.onNext(customer to ResponseType.ADDED)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Logger.log("getCustomerbySalonCustomerKey : FAILED: ${databaseError.code.toString()}")
                    }
                })

    }




}