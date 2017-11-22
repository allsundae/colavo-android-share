package com.colavo.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.entity.event.EventModel
import kotlinx.android.synthetic.main.event_item.view.*

/**
 * Created by RUS on 24.07.2016.
 */
class EventAdapter(val onItemClickListener: OnItemClickListener, val items: MutableList<EventModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: EventModel)
        fun onLongItemClicked(item: EventModel)
    }

    inner class ItemViewHolder(val v: View,
                               val eventText: TextView = v.message_text,
                               val eventUserAndTime: TextView = v.event_user_and_time) : RecyclerView.ViewHolder(v) {

        fun bind(eventModel: EventModel) {
            this.eventText.text = eventModel.salon_key
      //      this.eventUserAndTime.text = "${eventModel.userName}, ${eventModel.time.toString(PATTERN)}"
            this.eventUserAndTime.text = "${eventModel.begin_at}, ${eventModel.begin_at.toString()}"

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END

            if(eventModel.booked_by_customer)
                params.gravity = Gravity.END
            else
                params.gravity = Gravity.START
            this.eventText.layoutParams = params
            this.eventUserAndTime.layoutParams = params

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(eventModel) }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false))

    override fun getItemCount(): Int = items.size

    companion object {
        val PATTERN: String = "HH:mm yy/MM/dd"
    }

}