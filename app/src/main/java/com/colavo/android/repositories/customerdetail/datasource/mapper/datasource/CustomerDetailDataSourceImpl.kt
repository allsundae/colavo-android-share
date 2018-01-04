package com.colavo.android.repositories.customerdetail.datasource

import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customerdetail.CustomerDetailEntity
import com.colavo.android.entity.customerdetail.CustomerDetailModel
import com.colavo.android.entity.query.customerdetail.CustomerDetailQuery
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.net.FirebaseAPI
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

    override fun initialize(query: CustomerDetailQuery.GetCustomerEvents): Observable<Pair<CustomerDetailModel, ResponseType>>
            = Observable.create<Pair<CustomerDetailEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("customer_events")
                        .child(query.customerUid)
                        .orderByChild("begin_at")//.equalTo(getCurrentFirebaseUserKey())
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CustomerDetailEntity::class.java)
                                        customerDetail.id = dataSnapshot.key
                                        Logger.log("CUSTOMERDETAIL changed ${customerDetail.id}")
                                        subscriber.onNext(customerDetail to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CustomerDetailEntity::class.java)
                                        customerDetail.id = dataSnapshot.key

                                        val numOfServices: Long = dataSnapshot.child("services").childrenCount

                                        Logger.log("(1) CUSTOMERDETAIL ADDED : event_key : ${customerDetail.id} (${numOfServices})")

                      /*                  for (num in numOfServices) {

                                        }

                                        customerDetail.customer_menu = dataSnapshot.child("services").childrenCount
                                        Logger.log("(5) added event ${customerDetail.customer_name} : ${service.name} : ${service.key}")
*/
                                        subscriber.onNext(customerDetail to ResponseType.ADDED)
                                    }
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                                    if(dataSnapshot != null) {
                                        val customerDetail = dataSnapshot.getValue(CustomerDetailEntity::class.java)
                                        customerDetail.id = dataSnapshot.key
                                        subscriber.onNext(customerDetail to ResponseType.REMOVED)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError?) {
                                    subscriber.onError(error?.toException())
                                }

                        })
            }
            /*. DEVELOPING */
//            .concatMap { response -> convertToCustomerDetailModel(response)}
/* TODO This is WORKING on GITHUB */
            .concatMapEager {
                pair -> Observable.zip(Observable.just(pair), Observable.just(pair.first)//getCustomerDetailbySalonKey(pair.first.salon_key) //getCustomerDetailbySalonKey(pair.first.salon_key) //, getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key)//getEventbySalonEventKey(pair.first.salon_key, pair.first.event_key)
                                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                        , { pair, test -> CustomerDetailMapper.transformFromEntity(pair.first) to pair.second }
                                        )
                            }


    private fun convertToCustomerDetailModel(pair: Pair<CustomerDetailEntity, ResponseType>) : Observable< Pair<CustomerDetailModel, ResponseType>> {
        Logger.log("convertToCustomerDetailModel : customerDetail_uid: ${pair.first.id}, customer_key: ${pair.first.customer_key}")

        var customer : CustomerEntity? = null

        getCustomerbySalonCustomerKey(pair.first.salon_key, pair.first.customer_key, object : SimpleCallback<CustomerEntity>{
            override fun callback(customerCallback: CustomerEntity){
                customer = customerCallback
                Logger.log("getCustomerbySalonCustomerKey : Callback : ${customerCallback.name} -> var customer : ${customer!!.name}")
            }
        })

        return Observable.just(pair)
                .concatMapEager { test ->
                    Observable.zip(Observable.just(pair.first), Observable.just(customer!!))
                    { WTF, arigato -> CustomerDetailMapper.transformFromEntity(pair.first) to pair.second }
                } .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

    }

    private fun getCustomerbySalonKey(salon_key: String?) : Observable<CustomerEntity>  {
        Logger.log("getCustomerbySalonKey: ${salon_key}")
        return retrofit.create(FirebaseAPI::class.java).getCustomerbySalonKey(salon_key ?: "")
}



   /* override fun createCustomerDetail(query: CustomerDetailQuery.CreateCustomerDetail): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createCustomerDetail(query.salon_key
                            , CustomerDetailEntity(customerDetail_uid = query.customerDetail_uid
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
                                                ))*/



    private fun getCustomerbySalonCustomerKey(salon_key: String?, customer_key: String?, finishedCallback: SimpleCallback<CustomerEntity>)
    {
        val firebaseDatabase3: FirebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase3.reference.child("salon_customers")
                //    .child(customerDetail.author_employee_key)
                .child(salon_key)
                .child(customer_key)
                .addListenerForSingleValueEvent( object :ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val customer  = dataSnapshot.getValue(CustomerEntity::class.java)
 //                       customer.image_urls.image_thumb_url = (dataSnapshot.child("image_url").child("thumb").value).toString()
                        Logger.log("(2.5) getCustomerbySalonCustomerKey : customer_name: ${customer.name} (${customer.image_urls.image_thumb_url})")
                        finishedCallback.callback(customer)
                        //subscriber.onNext(customer to ResponseType.ADDED)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Logger.log("getCustomerbySalonCustomerKey : FAILED: ${databaseError.code.toString()}")
                    }
                })

    }




}