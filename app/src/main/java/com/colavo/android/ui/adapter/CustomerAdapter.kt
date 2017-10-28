package com.colavo.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.entity.customer.CustomerModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.customer_item.view.*

class CustomerAdapter(val onItemClickListener: OnItemClickListener
                      , val items: MutableList<CustomerModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: CustomerModel)
        fun onLongItemClicked(item: CustomerModel)
    }

    inner class ItemViewHolder(val v: View,
                               val customerName: TextView = v.customer_name,
                               val customerPhone: TextView = v.customer_phone,
                               val customerImage: ImageView = v.customer_image) : RecyclerView.ViewHolder(v) {

        init {
          //TODO
        }

        fun bind(customerModel: CustomerModel) {
            val context = itemView.context
            this.customerName.text = customerModel.name
            this.customerPhone.text = customerModel.phone
           // this.customerImage.loadUrl(customerModel.image_url)
/*
            if (customerModel.image_url != null) {
                Picasso.with(context)
                        .load(customerModel.image_url)
                        .resize(50, 50)
                        .centerCrop()
                        .into(this.customerImage)
            }
*/

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(customerModel) }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.customer_item, parent, false))

    override fun getItemCount(): Int = items.size

}