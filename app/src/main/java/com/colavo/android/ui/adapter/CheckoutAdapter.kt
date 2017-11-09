package com.colavo.android.ui.adapter

import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.entity.checkout.CheckoutModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.checkout_item.view.*

class CheckoutAdapter(val onItemClickListener: OnItemClickListener
                      , val items: MutableList<CheckoutModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: CheckoutModel, position: Int)
        fun onLongItemClicked(item: CheckoutModel)
    }

    inner class ItemViewHolder(val v: View,
                               val checkoutName: TextView = v.checkout_customer_name,
                               val checkoutMenu: TextView = v.checkout_menu,
                               val checkoutImage: ImageView = v.checkout_customer_image,
                               val checkoutMemo: TextView = v.checkout_memo) : RecyclerView.ViewHolder(v) {

        init {
          //TODO
        }

        fun bind(checkoutModel: CheckoutModel) {
            val context = itemView.context
            this.checkoutName.text = checkoutModel.event_key
           // this.checkoutMenu.text = checkoutModel.paid_types[position].name
           // this.checkoutImage.loadUrl(checkoutModel.image)
//            val thisThumbImage:String = checkoutModel.image_urls!!.getThumbUrl()
/*
           if (checkoutModel.image_urls[0].image_thumb_url != "") {
               val transForm = CircleTransform()

                Picasso.with(context)
                        .load(checkoutModel.image_urls[0].image_thumb_url) //"https://firebasestorage.googleapis.com/v0/b/jhone-364e5.appspot.com/o/profile.jpeg?alt=media&token=f267631e-f6fd-4c90-bace-e7cc823442bb"
                        .resize(240, 240)
                        .centerCrop()
                        .placeholder(R.drawable.ic_checkout_holder_person)
                        .transform(transForm)
                        .into(this.checkoutImage)

            }
*/

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(checkoutModel, position) }
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.checkout_item, parent, false))

    override fun getItemCount(): Int = items.size

}