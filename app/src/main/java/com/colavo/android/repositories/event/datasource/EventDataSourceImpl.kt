package com.colavo.android.repositories.event.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.entity.query.event.EventQuery
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.net.FirebaseAPI
import com.colavo.android.repositories.event.datasource.mapper.EventMapper
import retrofit2.Retrofit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by RUS on 23.07.2016.
 */
class EventDataSourceImpl(val retrofit: Retrofit, val firebaseDatabase: FirebaseDatabase, val firebaseAuth: FirebaseAuth) : EventDataSource {

    override fun getEventMessages(query: EventQuery.GetSalonEvents): Observable<Pair<EventModel, ResponseType>> = Observable.create<Pair<EventEntity, ResponseType>>
    { subscriber -> firebaseDatabase.reference
                .child("events")
                .orderByChild("salonId")
                .equalTo(query.salonId)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
                        if(dataSnapshot != null) {
                            val event = dataSnapshot.getValue(EventEntity::class.java)
                            event.id = dataSnapshot.key
                            subscriber.onNext(event to ResponseType.CHANGED)
                        }
                    }

                    override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                        if(dataSnapshot != null) {
                            val event = dataSnapshot.getValue(EventEntity::class.java)
                            event.id = dataSnapshot.key
                            subscriber.onNext(event to ResponseType.ADDED)
                        }
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                        if(dataSnapshot != null) {
                            val event = dataSnapshot.getValue(EventEntity::class.java)
                            event.id = dataSnapshot.key
                            subscriber.onNext(event to ResponseType.REMOVED)
                        }
                    }

                    override fun onCancelled(error: DatabaseError?) {
                        subscriber.onError(error?.toException())
                    }
                })
    }
            .flatMap {
                pair -> Observable.zip(Observable.just(pair),
                                                getUserById(pair.first.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()),
                                                { pair, user -> EventMapper.transformFromMessageEntityAndUser(pair.first, user, user.uid.equals(firebaseAuth.currentUser?.uid)) to pair.second }
                                        ) }

    override fun sendEvent(query: EventQuery.SendEvent): Observable<FirebaseResponse> {
        val event = EventEntity(salonId = query.salonId, text = query.text, time = query.time)
        event.userId = firebaseAuth.currentUser?.uid.toString()
        return retrofit.create(FirebaseAPI::class.java).sendEvent(event)
                .doOnNext { response -> event.id = response.id }
                .doOnNext { updateEvent(event) }
    }

    private fun getUserById(userId: String) = retrofit.create(FirebaseAPI::class.java).getUserById(userId)

    private fun updateEvent(eventEntity: EventEntity) {
        val map = mutableMapOf<String, Any>("lastEventId" to eventEntity.id)
        firebaseDatabase.reference
                .child("salons")
                .child(eventEntity.salonId)
                .updateChildren(map)
    }

}