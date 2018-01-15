package com.colavo.android.ui.adapter

import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.colavo.android.R
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.utils.ConvertTimestampToDateandTime
import com.colavo.android.utils.Logger
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.checkout_item.view.*
import android.support.v4.view.PagerAdapter.POSITION_NONE
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat.getDrawable
import android.widget.LinearLayout


class CheckoutAdapter(val onItemClickListener: OnItemClickListener
                      , val items: MutableList<CheckoutModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: CheckoutModel, position: Int, v: View)
        fun onLongItemClicked(item: CheckoutModel)
    }

    inner class ItemViewHolder(val v: View,
                               val checkoutName: TextView = v.checkout_customer_name,
                               val checkoutMenu: TextView = v.checkout_menu,
                               val checkoutImage: ImageView = v.checkout_customer_image,
                               val checkoutMemo: TextView = v.checkout_memo,
                               val checkoutButton: TextView = v.btn_checkout,
                               val checkoutButtonContainer: LinearLayout = v.btn_checkout_container,
                               val checkoutButtonIcon: ImageView = v.btn_checkout_icon,
                               val checkoutTime: TextView = v.checkout_time_ampm

                            ) : RecyclerView.ViewHolder(v) {

        init {
          //TODO
        }

        fun bind(checkoutModel: CheckoutModel) {
            val context = itemView.context
            this.checkoutName.text = checkoutModel.customer_name
            this.checkoutTime.text = ConvertTimestampToDateandTime(checkoutModel.created_at.toLong(), "a\nh:mm")
            this.checkoutMenu.text = checkoutModel.service_menus
            if (checkoutModel.memo_txt != "")  this.checkoutMemo.text =  checkoutModel.memo_txt

            if (checkoutModel.checkout_key != null && checkoutModel.checkout_key != ""){
                if (checkoutModel.checkout_price != "") {
                  //  val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, )
                    this.checkoutButton.text = checkoutModel.checkout_price
                    //this.checkoutButtonContainer.setBackgroundColor(getColor(context, R.color.checkoutMemoTextColor))
                    this.checkoutButtonContainer.setBackgroundResource(R.drawable.ic_button_line_checkout)
//                    this.checkoutButtonContainer.setLayoutParams(lp)

                    if (checkoutModel.checkout_paid_type == "credit_card"){
                        checkoutButtonIcon.setImageResource(R.drawable.ic_creditcard)
                    }
                    else{
                        checkoutButtonIcon.setImageResource(R.drawable.ic_cash)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkoutButtonIcon.setColorFilter(context.getColor(R.color.colorAccent))
                    }
                    checkoutButtonIcon.visibility = View.VISIBLE


                   /* var img: Drawable? = null

                    if (checkoutModel.checkout_paid_type == "credit_card"){
                        img = context.getResources().getDrawable(R.drawable.ic_creditcard)
                    }
                    else{
                        img = context.getResources().getDrawable(R.drawable.ic_cash)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        img!!.setColorFilter(context.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN )
                    }
                    img!!.setBounds(0, 0, 60, 0)
                    this.checkoutButton.setCompoundDrawables(img, null, null, null)*/

                }
            }


           // this.checkoutImage.loadUrl(checkoutModel.image)
//            val thisThumbImage:String = checkoutModel.image_urls!!.getThumbUrl()

           if (checkoutModel.customer_image_thumb != "") {
               val transForm = CustomerAdapter.CircleTransform()

                Picasso.with(context)
                        .load(checkoutModel.customer_image_thumb) //"https://firebasestorage.googleapis.com/v0/b/jhone-364e5.appspot.com/o/profile.jpeg?alt=media&token=f267631e-f6fd-4c90-bace-e7cc823442bb"
                        .resize(240, 240)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_container)
                        .transform(transForm)
                        .into(this.checkoutImage)
            }

            Logger.log("CheckoutAdapter : bind : ${checkoutModel.customer_name} (${position}) \t ${checkoutModel.customer_image_thumb}")

            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(checkoutModel, position, v) }
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.checkout_item, parent, false))

    override fun getItemCount(): Int = items.size

    fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}