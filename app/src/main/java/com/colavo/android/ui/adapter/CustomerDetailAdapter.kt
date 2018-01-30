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
import android.os.Build
import android.widget.Button
import android.widget.LinearLayout
import com.colavo.android.R.id.*
import com.colavo.android.entity.checkout.CheckoutModel
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.utils.CircleTransform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.customer_detail_item.view.*
import org.w3c.dom.Text

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
                               val checkoutButton: TextView = v.btn_customer_detail,
                               val checkoutButtonContainer: LinearLayout = v.btn_customer_detail_container,
                               val checkoutButtonIcon: ImageView = v.btn_customer_detail_icon,
                               val checkoutTime: TextView = v.customer_detail_time_ampm
                            ) : RecyclerView.ViewHolder(v) {

        init {
          //TODO
        }

        fun bind(checkoutModel: CheckoutModel) {
            val context = itemView.context
            var newCustomer = CustomerEntity()

            val mDatabase = FirebaseDatabase.getInstance().getReference().child("salon_customers").child(checkoutModel.salon_key).child(checkoutModel.customer_key)
            mDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    newCustomer = dataSnapshot.getValue(CustomerEntity::class.java)!!
                    Logger.log ("CustomerDetailAdapter onDataChange : ${newCustomer.name.toString()}")
                    event.customer_name = newCustomer.name
                    event.customer_image_full = newCustomer.image_url.full
                    event.customer_image_thumb = newCustomer.image_url.thumb
                    event.customer_phone = newCustomer.phone
                    event.customer_fund = newCustomer.fund

                    checkoutName.text = event.customer_name

                    if (newCustomer.image_url.thumb != ""){
                        Picasso.with(context)
                                .load(newCustomer.image_url.thumb)
                                .resize(280, 280)
                                .centerCrop()
                                .noPlaceholder()
                                .into(checkoutImage)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Logger.log("Failed to read value." + error.toException().toString())
                }
            })

            this.checkoutName.text = event.customer_name //customerDetailModel.customer_name
            this.checkoutTime.text = ConvertTimestampToDateandTime(checkoutModel.begin_at.toLong(), "MM/dd\nE") //this.checkoutTime.text = ConvertTimestampToDateandTime(customerDetailModel.begin_at.toLong(), "a\nh:mm")
            this.checkoutMenu.text = checkoutModel.service_menus
            if (checkoutModel.memo_txt != "")  this.checkoutMemo.text =  checkoutModel.memo_txt
            if (checkoutModel.checkout_key != null && checkoutModel.checkout_key != ""){
                if (checkoutModel.checkout_price != "") {
                    //  val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, )
                    this.checkoutButton.text = checkoutModel.checkout_price
                    //this.checkoutButtonContainer.setBackgroundColor(getColor(context, R.color.checkoutMemoTextColor))
                    this.checkoutButtonContainer.setBackgroundResource(R.drawable.ic_button_line_checkout)
//                    this.checkoutButtonContainer.setLayoutParams(lp)

                    if (checkoutModel.checkout_paid_type == "credit_card"){ checkoutButtonIcon.setImageResource(R.drawable.ic_creditcard) }
                    else{checkoutButtonIcon.setImageResource(R.drawable.ic_cash)}

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {checkoutButtonIcon.setColorFilter(context.getColor(R.color.colorAccent)) }
                    checkoutButtonIcon.visibility = View.VISIBLE
                }
            }

           if (event.customer_image_thumb != "") {
//               val transForm = CircleTransform()
                Picasso.with(context)
                        .load(event.customer_image_thumb) //"https://firebasestorage.googleapis.com/v0/b/jhone-364e5.appspot.com/o/profile.jpeg?alt=media&token=f267631e-f6fd-4c90-bace-e7cc823442bb"
                        .resize(240, 240)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_container)
//                        .transform(transForm)
                        .into(this.checkoutImage)
            }else{
               this.checkoutImage.setImageResource(R.drawable.ic_person_container)
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