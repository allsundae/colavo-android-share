package com.colavo.android.repositories.customer.datasource

import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.customer.ImageUrl
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
                        .orderByChild("name").limitToFirst(500)
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
                                    //    val urls:ImageUrl = dataSnapshot.child("image_url").child("thumb").getValue(ImageUrl::class.java)

                                        if ( dataSnapshot.child("image_url").child("thumb").value != null) {
                                            customer.image_urls[0]!!.image_thumb_url = (dataSnapshot.child("image_url").child("thumb").value).toString()
                                            Logger.log("Image Thumb Url : ${dataSnapshot.child("image_url").child("thumb").value.toString()}")
                                            Logger.log("Image Thumb Url : ${customer.image_urls[0].image_thumb_url}")
                                        }

                                        if ( dataSnapshot.child("image_url").child("full").value != null) {
                                            customer.image_urls[0]!!.image_full_url = (dataSnapshot.child("image_url").child("full").value).toString()
                                        }


                                                //dataSnapshot.child("image_url").child("thumb").getValue<ImageUrl>(ImageUrl::class.java).toString()
                                    //    customer.image_urls?.image_thumb_url = "https://firebasestorage.googleapis.com/v0/b/colavo-ae9bd.appspot.com/o/images%2Fcustomers%2F-KusC3nS4hFb0KfQiCy9%2Fprofiles%2Fprofile_thumb.png?alt=media&token=44a4b1fa-e1a7-4e29-9a7a-a54009a2c6ac"//dataSnapshot.child("image_url").child("thumb").value.toString()
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
          .concatMapEager { pair -> Observable.zip(Observable.just(pair), getCustomerbySalonKey(pair.first.uid)
                  .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        , { pair, u -> CustomerMapper.transformFromEntity(pair.first) to pair.second }
                     ) }

/*
            .concatMap { response -> convertToCustomerModel(response)  }

    private fun convertToCustomerModel(pair: Pair<CustomerEntity, ResponseType>)
              :Observable<Pair<CustomerModel, ResponseType>> {
                        return getCustomerbySalonKey(pair.first.uid)
                                .concatMapEager {customer -> Observable.zip(Observable.just(customer), getCustomerbySalonKey(customer?.uid))
                                                    {customer, user -> CustomerMapper.transformFromEntity(pair.first) to pair.second }
                                                }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                 }
*/

    override fun createCustomer(query: CustomerQuery.CreateCustomer): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createCustomer(query.salonUid
                            , CustomerEntity(uid = query.customerUid, phone = query.customerPhone
                                                , name = query.customerName, image_urls = query.customerImageUrls))

/*    private fun convertToCustomerModel(pair: Pair<CustomerEntity, ResponseType>)
            : Observable<Pair<CustomerModel, ResponseType>> {
        return getCustomerbySalonKey((pair.first).uid)
                .concatMap { customers -> Observable.zip(Observable.just(customers), getCustomerbySalonKey(customers?.uid))
                              { customers, customer -> CustomerMapper.createCustomerWithEventAndUser(pair.first, customer) to pair.second }
                             }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/


    private fun getCustomerbySalonKey(uid: String?)
            : Observable<CustomerEntity> = retrofit.create(FirebaseAPI::class.java).getCustomerBySalonId(uid ?: "")

/*    private fun getNumberofCustomer : Int (){

        val numOfCustomers: Int = datasnapshot.getChildrenCount()
        return val
    }
    */

}