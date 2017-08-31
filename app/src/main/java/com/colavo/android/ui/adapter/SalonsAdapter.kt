package com.colavo.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.entity.salon.SalonModel
import kotlinx.android.synthetic.main.salon_item.view.*

/**
 * Created by RUS on 17.07.2016.
 */
class SalonsAdapter(val onItemClickListener: OnItemClickListener, val items: MutableList<SalonModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: SalonModel)
        fun onLongItemClicked(item: SalonModel)
    }

    inner class ItemViewHolder(val v: View,
                               val salonName: TextView = v.salon_name,
/*                               val conversationLastMessageTime: TextView = v.conversation_last_message_time,
                               val conversationLastMessage: TextView = v.conversation_last_message,*/
                               val salonAddress: TextView = v.salon_address) : RecyclerView.ViewHolder(v) {

/*        init {
            this.salonName.typeface = MediumTypeface.getInstance(v.context)
        }*/

        fun bind(salonModel: SalonModel) {
            this.salonName.text = salonModel.name
            this.salonAddress.text = salonModel.address
         /*   this.conversationLastMessageTime.text = salonModel.lastEventTime*/

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(salonModel) }
        }

/*        object MediumTypeface {
            private var typeface: Typeface? = null

            fun getInstance(context: Context): Typeface {
                if(typeface == null)
                    return Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")
                else return typeface as Typeface
            }
        }*/
    }
 
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.salon_item, parent, false))

    override fun getItemCount(): Int = items.size

}