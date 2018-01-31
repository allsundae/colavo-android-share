package com.colavo.android.ui.adapter

import android.content.Context
import android.graphics.*
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.R.drawable.*
import com.colavo.android.entity.customer.CustomerModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.customer_item.view.*


class CustomerAdapter(val onItemClickListener: OnItemClickListener
                      , val items: MutableList<CustomerModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
        //, FastScrollRecyclerView.SectionedAdapter, FastScrollRecyclerView.MeasurableAdapter
        {


    interface OnItemClickListener {
        fun onItemClicked(item: CustomerModel, position: Int, v: View)
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
            this.customerPhone.text = customerModel.national_phone
      //      Picasso.with(customerImage!!.context).cancelRequest(customerImage!!)

           if (customerModel.image_url.thumb != "" && customerModel.image_url.thumb != null && customerModel.image_url != null) {
               //val transForm = CircleTransform()
                Picasso.with(context)
                        .load(customerModel.image_url.thumb)
                        .resize(240, 240)
                        .centerCrop()
                        .placeholder(ic_person_container)
                        .error(ic_person_container)
                      //  .transform(transForm)
                        .into(this.customerImage)
            }else{
               this.customerImage.setImageResource(R.drawable.ic_person_container)
           }


            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(customerModel, position, v) }
        }

    }

/*
   override fun getSectionName(position: Int): String {
        return position.toString()
    }
    override fun getViewTypeHeight(recyclerView: RecyclerView?, viewType: Int): Int {
        */
/*val params = recyclerView?.layoutParams
        return params!!.height
        *//*

        //val px : Int = convertDpToPixel(60f, ).toInt()
        return 240
       // showToast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
*/



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.customer_item, parent, false))

    override fun getItemCount(): Int = items.size

    fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}