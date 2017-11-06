package com.colavo.android.ui.adapter

import android.graphics.*
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
import android.graphics.Shader.TileMode
import com.squareup.picasso.Transformation



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
           // this.customerImage.loadUrl(customerModel.image)
//            val thisThumbImage:String = customerModel.image_urls!!.getThumbUrl()

           if (customerModel.image_urls[0].image_thumb_url != "") {
               val transForm = CircleTransform()

                Picasso.with(context)
                        .load(customerModel.image_urls[0].image_thumb_url) //"https://firebasestorage.googleapis.com/v0/b/jhone-364e5.appspot.com/o/profile.jpeg?alt=media&token=f267631e-f6fd-4c90-bace-e7cc823442bb"
                        .resize(60, 60)
                        .centerCrop()
                        .placeholder(R.drawable.ic_customer_holder_person)
                        .transform(transForm)
                        .into(this.customerImage)

            }


            this.itemView.setOnClickListener { onItemClickListener.onItemClicked(customerModel) }
        }

    }

    public class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.setShader(shader)
            paint.setAntiAlias(true)

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
            = (holder as ItemViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?
            = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.customer_item, parent, false))

    override fun getItemCount(): Int = items.size

}