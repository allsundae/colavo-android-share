package com.colavo.android.repositories.salons.datasource

import com.google.firebase.database.*
import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.salon.SalonEntity
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.repositories.salons.datasource.mapper.SalonMapper
import com.colavo.android.entity.session.User
import com.colavo.android.entity.query.salons.SalonsQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.net.FirebaseAPI
import com.colavo.android.utils.Logger
import retrofit2.Retrofit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by RUS on 17.07.2016.
 */
class SalonsDataSourceImpl @Inject constructor(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase) : SalonsDataSource {

    override fun initialize(query: SalonsQuery.GetSalons): Observable<Pair<SalonModel, ResponseType>>
            = Observable.create<Pair<SalonEntity, ResponseType>>
                { subscriber -> firebaseDatabase.reference.child("salons")
                        .orderByChild("owner_uid").equalTo(query.ownerUid).addChildEventListener(object : ChildEventListener {
                    override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon?.id = dataSnapshot.key
                            Logger.log("changed ${salon?.name}")
                            subscriber.onNext(salon!! to ResponseType.CHANGED)
                        }
                    }

                    override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon?.id = dataSnapshot.key
                            Logger.log("added ${salon?.name}")
                            subscriber.onNext(salon!! to ResponseType.ADDED)
                        }
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon?.id = dataSnapshot.key
                            subscriber.onNext(salon!! to ResponseType.REMOVED)
                        }
                    }

                    override fun onCancelled(error: DatabaseError?) {
                        subscriber.onError(error?.toException())
                    }

                })
            }
           // .concatMap { response -> convertToSalonModel(response)  }
            .concatMapEager { pair -> Observable.zip(Observable.just(pair),  getUserById(pair.first.owner_uid)
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            , { pair, user -> SalonMapper.transformFromEntity(pair.first, user) to pair.second }
                )}

    override fun createSalon(query: SalonsQuery.CreateSalon): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createSalon(SalonEntity(name = query.salonName, address = query.salonAddress, owner_uid = query.ownerUid))

    private fun convertToSalonModel(pair: Pair<SalonEntity, ResponseType>) : Observable<Pair<SalonModel, ResponseType>> {
        Logger.log("convertToSalonModel : ${pair.first.name}")
        return getUserById((pair.first).owner_uid)
                .concatMapEager { test -> Observable.zip(Observable.just(test),  getUserById(test?.uid))
                                    { WTF, user -> SalonMapper.transformFromEntity(pair.first, user) to pair.second }
                                }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }




    private fun setIdToSalon(salonEntity: SalonEntity, id: String): SalonEntity {
        salonEntity.id = id
        return salonEntity
    }
    
    private fun getEventBySalonId(salonId: String?): Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventById(salonId ?: "")

    private fun getOwnerName(userId: String?) : Observable<User> = retrofit.create(FirebaseAPI::class.java).getUserById(userId ?: "")
    
    private fun getUserById(userId: String?)
            : Observable<User> = retrofit.create(FirebaseAPI::class.java).getUserById(userId ?: "")

}