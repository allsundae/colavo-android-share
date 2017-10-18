package com.colavo.android.presenters.event

import android.os.SystemClock
import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.response.ResponseType
import com.colavo.android.entity.response.FirebaseResponse
import com.colavo.android.interactors.event.GetSalonEvents
import com.colavo.android.interactors.event.SendEvent
import com.colavo.android.ui.event.eventView
import com.colavo.android.utils.Logger
import com.google.firebase.database.ServerValue
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import rx.Subscriber
import java.sql.Timestamp
import javax.inject.Inject
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference





/**
 * Created by RUS on 23.07.2016.
 */
class EventPresenterImpl @Inject constructor(val getSalonEvents: GetSalonEvents,
                                             val sendEvent: SendEvent) : EventPresenter {

    var eventView: eventView? = null
    lateinit var salonId: String

    override fun attachView(eventView: eventView) {
        this.eventView = eventView
    }

    override fun initialize(salonId: String) {
        this.salonId = salonId
        Logger.log(salonId)
        getSalonEvents.execute(salonId, EventSubscriber())
    }

    override fun sendEvent(eventText: String) {
        val time = DateTime.now(DateTimeZone.getDefault()).toString()
// firebase.database.ServerValue.TIMESTAMP
        val timestamp = SystemClock.currentThreadTimeMillis()

      //  val timestamp : Map<String, String> = ServerValue.TIMESTAMP
        sendEvent.execute(salonId, eventText, timestamp, SendEventSubscriber() )


    }

    override fun onDestroy() {
        this.eventView = null
        //this.chatUseCase.unsubscribe()
    }

    private inner class EventSubscriber : Subscriber<Pair<EventModel, ResponseType>>() {

        override fun onCompleted() {
        }

        override fun onError(throwable: Throwable) {
            eventView?.onError(throwable)
        }

        override fun onNext(pair: Pair<EventModel, ResponseType>) {
            when(pair.second) {
                ResponseType.ADDED -> eventView?.addEvent(pair.first)
                ResponseType.CHANGED -> eventView?.changeEvent(pair.first)
                ResponseType.REMOVED -> eventView?.removeEvent(pair.first)
            }
        }

    }

    private inner class SendEventSubscriber : Subscriber<FirebaseResponse>() {
        override fun onNext(firebaseResponse: FirebaseResponse?) {
        }

        override fun onCompleted() {
        }

        override fun onError(throwable: Throwable?) {
            if(throwable != null) eventView?.onError(throwable)
        }

    }
}