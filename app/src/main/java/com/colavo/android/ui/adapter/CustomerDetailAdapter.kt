package com.colavo.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.utils.ConvertTimestampToDateandTime
import com.colavo.android.utils.Logger
import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Button
import com.colavo.android.entity.checkout.CheckoutModel
import kotlinx.android.synthetic.main.customer_detail_item.view.*

class CustomerDetailAdapter(val onItemClickListener: OnItemClickListener
                            , val items: MutableList<CheckoutModel>
                            , val event : CheckoutModel   ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: CheckoutModel, position: Int, v: View)
        fun onLongItemClicked(item: CheckoutModel)
    }

    inner class ItemViewHolder(val v: View,
                               val checkoutName: TextView = v.customer_detail_customer_name,
                               val checkoutMenu: TextView = v.customer_detail_menu,
                               val checkoutImage: ImageView = v.customer_detail_customer_image,
                               val checkoutMemo: TextView = v.customer_detail_memo,
                               val checkoutButton: Button = v.btn_customer_detail,
                               val checkoutTime: TextView = v.customer_detail_time_ampm
                            ) : RecyclerView.ViewHolder(v) {

        init {
          //TODO
        }

        fun bind(checkoutModel: CheckoutModel) {
            val context = itemView.context
            //val textview: TextView = container_1stline as TextView
            //val str: String = container_1stline.text.toString()
           // this.checkoutName.text = container_1st.text.toString()//customerDetailModel.customer_name

            this.checkoutName.text = event.customer_name //customerDetailModel.customer_name
            this.checkoutTime.text = ConvertTimestampToDateandTime(checkoutModel.begin_at.toLong(), "MM/dd\nE") //this.checkoutTime.text = ConvertTimestampToDateandTime(customerDetailModel.begin_at.toLong(), "a\nh:mm")
            this.checkoutMenu.text = checkoutModel.service_menus
            if (checkoutModel.memo_txt != "")  this.checkoutMemo.text =  checkoutModel.memo_txt
            if (checkoutModel.checkout_price != "") {
                this.checkoutButton.text = checkoutModel.checkout_price
            }

           if (event.customer_image_thumb != "") {
               val transForm = CustomerAdapter.CircleTransform()

                Picasso.with(context)
                        .load(event.customer_image_thumb) //"https://firebasestorage.googleapis.com/v0/b/jhone-364e5.appspot.com/o/profile.jpeg?alt=media&token=f267631e-f6fd-4c90-bace-e7cc823442bb"
                        .resize(240, 240)
                        .centerCrop()
                        .placeholder(R.drawable.ic_customer_holder_person)
                        .transform(transForm)
                        .into(this.checkoutImage)
            }

            Logger.log("CustomerDetailAdapter : bind : ${event.customer_name} : ${event.customer_image_thumb}")

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(checkoutModel, position, v) }
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.customer_detail_item, parent, false))

    override fun getItemCount(): Int = items.size

}