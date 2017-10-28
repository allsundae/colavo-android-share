package com.colavo.android.repositories.customer.datasource

import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.CustomerModel
import com.google.firebase.database.*
import com.colavo.android.entity.query.customer.CustomerQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.net.FirebaseAPI
import com.colavo.android.repositories.customer.datasource.mapper.CustomerMapper
import com.colavo.android.utils.Logger
import retrofit2.Retrofit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


class CustomerDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : CustomerDataSource {

    override fun initialize(query: CustomerQuery.GetCustomer): Observable<Pair<CustomerModel, ResponseType>>
            = Observable.create<Pair<CustomerEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("salon_customers")
                     //   .orderByChild("key").equalTo(query.salonUid)
                        .child(query.salonUid)
                        .addChildEventListener(object : ChildEventListener {
                                override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                }

                                override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customer = dataSnapshot.getValue(CustomerEntity::class.java)
                                        customer.uid = dataSnapshot.key
                                        Logger.log("changed ${customer.name}")
                                        subscriber.onNext(customer to ResponseType.CHANGED)
                                    }
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                                    if(dataSnapshot != null) {
                                        val customer = dataSnapshot.getValue(CustomerEntity::class.java)
                                        customer.uid = dataSnapshot.key
                                        Logger.log("added ${customer.name}")
                                        subscriber.onNext(customer to ResponseType.ADDED)
                                    }
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                                    if(dataSnapshot != null) {
                                        val customer = dataSnapshot.getValue(CustomerEntity::class.java)
                                        customer.uid = dataSnapshot.key
                                        subscriber.onNext(customer to ResponseType.REMOVED)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError?) {
                                    subscriber.onError(error?.toException())
                                }

                            })
            }
 /*           .flatMap { response -> convertToCustomerModel(response)  }*/
            .flatMap { pair -> Observable.zip(Observable.just(pair)
                    , getCustomerbySalonKey(pair.first.uid).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    , { pair, user -> CustomerMapper.transformFromEntity(pair.first) to pair.second }
            ) }

    override fun createCustomer(query: CustomerQuery.CreateCustomer): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createCustomer(query.salonUid, CustomerEntity(uid = query.customerUid, phone = query.customerPhone
                            , name = query.customerName, image_url = query.customerImageUrl))

/*    private fun convertToCustomerModel(pair: Pair<CustomerEntity, ResponseType>)
            : Observable<Pair<CustomerModel, ResponseType>> {
        return getCustomerbySalonKey((pair.first).uid)
                .concatMap { customers -> Observable.zip(Observable.just(customers), getCustomerbySalonKey(customers?.uid))
                              { customers, customer -> CustomerMapper.createCustomerWithEventAndUser(pair.first, customer) to pair.second }
                             }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/


    private fun getCustomerbySalonKey(uid: String?)
            : Observable<CustomerEntity> = retrofit.create(FirebaseAPI::class.java).getCustomerBySalonId(uid ?: "")
    

}