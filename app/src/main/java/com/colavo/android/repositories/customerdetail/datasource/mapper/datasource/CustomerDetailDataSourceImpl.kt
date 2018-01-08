package com.colavo.android.repositories.customerdetail.datasource

import com.colavo.android.entity.checkout.CheckoutEntity
import com.colavo.android.entity.checkout.CheckoutModel
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

                                        val numOfServices: Long = dataSnapshot.child("services").childrenCount

                                        Logger.log("(1) CUSTOMERDETAIL ADDED : event_key : ${customerDetail?.checkout_uid} (${numOfServices})")

                      /*                  for (num in numOfServices) {

                                        }

                                        customerDetail.customer_menu = dataSnapshot.child("services").childrenCount
                                        Logger.log("(5) added event ${customerDetail.customer_name} : ${service.name} : ${service.key}")
*/
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
            .concatMapEager { pair -> Observable.zip(Observable.just(pair), getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)//getCustomerDetailbySalonKey(pair.first.salon_key) //getCustomerDetailbySalonKey(pair.first.salon_key) //, getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)//getEventbySalonEventKey(pair.first.salon_key, pair.first.event_key)
                                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                        , { pair, customer -> CheckoutMapper.transformFromEntity(pair.first, customer) to pair.second }
                                        )
                            }




    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?) : Observable<CustomerEntity>  {
        Logger.log("(2) getCustomerbySalonCustomerKey: $salon_key, $customer_key")
        return retrofit.create(FirebaseAPI::class.java).getCustomerBySalonCustomerId(salon_key ?: "", customer_key ?: "")
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