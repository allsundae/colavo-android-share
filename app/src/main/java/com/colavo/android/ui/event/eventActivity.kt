package com.colavo.android.ui.event

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.entity.event.EventModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.event.EventPresenterImpl
import com.colavo.android.ui.adapter.EventAdapter
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.showSnackBar
import com.colavo.android.utils.toast
import kotlinx.android.synthetic.main.activity_event.*
import javax.inject.Inject

class eventActivity : AppCompatActivity(), eventView, EventAdapter.OnItemClickListener {

    @Inject
    lateinit var eventPresenter: EventPresenterImpl
    lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        (application as App).addEventComponent().inject(this)

        eventAdapter = EventAdapter(this, mutableListOf<EventModel>())
        events_recycler.adapter = eventAdapter

        val salon = intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        supportActionBar?.title = salon.name

        events_recycler.layoutManager = LinearLayoutManager(this)
        button_send.setOnClickListener { sendEvent() }

        eventPresenter.attachView(this)
        eventPresenter.initialize(salon.id)

    }

    private fun sendEvent() {
        eventPresenter.sendEvent(edit_event.text.toString())
        edit_event.setText("")
    }

    override fun setEvents(eventEntities: List<EventModel>) {
       events_recycler.adapter = EventAdapter(this, eventEntities.toMutableList()) //todo
    }


    override fun addEvent(eventEntity: EventModel) {
        eventAdapter.items.add(eventEntity)
        eventAdapter.notifyItemInserted(eventAdapter.itemCount)
    }

    override fun changeEvent(eventEntity: EventModel) {
        val position = eventAdapter.items.indexOfFirst { it.id.equals(eventEntity.id) }
        eventAdapter.items[position] = eventEntity
        eventAdapter.notifyItemChanged(position)
    }

    override fun removeEvent(eventEntity: EventModel) {
        val position = eventAdapter.items.indexOfFirst { it.id.equals(eventEntity) }
        eventAdapter.items.removeAt(position)
        eventAdapter.notifyItemRemoved(position)
    }

    override fun onItemClicked(item: EventModel) {
    }

    override fun onLongItemClicked(item: EventModel) {
    }

    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        toast(event)
    }

    override fun showSnackbar(event: String) {
        events_recycler.showSnackBar(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).clearEventComponent()
        eventPresenter.onDestroy()
    }
}
