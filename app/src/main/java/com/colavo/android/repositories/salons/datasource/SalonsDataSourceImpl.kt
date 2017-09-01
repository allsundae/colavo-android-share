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
            = Observable.create<Pair<SalonEntity, ResponseType>> { subscriber -> firebaseDatabase.reference.child("salons")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon.id = dataSnapshot.key
                            Logger.log("changed ${salon.name}")
                            subscriber.onNext(salon to ResponseType.CHANGED)
                        }
                    }

                    override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon.id = dataSnapshot.key
                            Logger.log("added ${salon.name}")
                            subscriber.onNext(salon to ResponseType.ADDED)
                        }
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                        if(dataSnapshot != null) {
                            val salon = dataSnapshot.getValue(SalonEntity::class.java)
                            salon.id = dataSnapshot.key
                            subscriber.onNext(salon to ResponseType.REMOVED)
                        }
                    }

                    override fun onCancelled(error: DatabaseError?) {
                        subscriber.onError(error?.toException())
                    }

                })
            }
            .flatMap { response -> convertToSalonModel(response)  }
    
    override fun createSalon(query: SalonsQuery.CreateSalon): Observable<FirebaseResponse>
            = retrofit.create(FirebaseAPI::class.java)
            .createSalon(SalonEntity(name = query.salonName))

    private fun convertToSalonModel(pair: Pair<SalonEntity, ResponseType>): Observable<Pair<SalonModel, ResponseType>> {
        return getOwnerName((pair.first).owner_uid).concatMap { users -> Observable.zip(Observable.just(users),
                    getUserById(users?.uid)) { users, user ->
                SalonMapper.createSalonWithEventAndUser(pair.first, user) to pair.second }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
/*   private fun convertToSalonModel(pair: Pair<SalonEntity, ResponseType>): Observable<Pair<SalonModel, ResponseType>> {
        return getLastEvent((pair.first).lastEventId).concatMap { events ->
            Observable.zip(Observable.just(events),
                    getUserById(events?.userId)) { events, user ->
                SalonMapper.createSalonWithEventAndUser(pair.first, events, user) to pair.second }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }  */


    /*private fun convertToSalonModel(pair: Pair<SalonEntity, ResponseType>): Observable<Pair<SalonModel, ResponseType>> {
        return getOwnerName((pair.first).owner_uid).concatMap { users ->
            Observable.zip(Observable.just(users),
                    getUserById(users?.uid)) { users, user ->
                SalonMapper.createSalonWithEventAndUser(pair.first, user) to pair.second }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }*/




    private fun setIdToSalon(salonEntity: SalonEntity, id: String): SalonEntity {
        salonEntity.id = id
        return salonEntity
    }
    
    private fun getLastEvent(eventId: String?): Observable<EventEntity> = retrofit.create(FirebaseAPI::class.java).getEventById(eventId ?: "")

    private fun getOwnerName(userId: String?) : Observable<User> = retrofit.create(FirebaseAPI::class.java).getUserById(userId ?: "")
    
    private fun getUserById(userId: String?): Observable<User> = retrofit.create(FirebaseAPI::class.java).getUserById(userId ?: "")

}