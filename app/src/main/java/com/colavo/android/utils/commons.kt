package com.colavo.android.utils

import android.content.Context
import android.graphics.*
import java.util.*
import android.util.DisplayMetrics
import com.squareup.picasso.Transformation
import java.security.AccessController.getContext
import java.text.NumberFormat


/**
 * Created by macbookpro on 2017. 10. 25..
 */

fun Int.getContrastColor(): Int {
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 128) Color.BLACK else Color.WHITE
}

fun Int.adjustAlpha(factor: Float): Int {
    val alpha = Math.round(Color.alpha(this) * factor)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun Int.getFormattedDuration(): String {
    val sb = StringBuilder(8)
    val hours = this / (60 * 60)
    val minutes = this % (60 * 60) / 60
    val seconds = this % (60 * 60) % 60

    if (this > 3600) {
        sb.append(String.format(Locale.getDefault(), "%02d", hours)).append(":")
    }

    sb.append(String.format(Locale.getDefault(), "%02d", minutes))
    sb.append(":").append(String.format(Locale.getDefault(), "%02d", seconds))
    return sb.toString()
}

// TODO: how to do "flags & ~flag" in kotlin?
fun Int.removeFlag(flag: Int) = (this or flag) - flag

fun convertDpToPixel(dp: Float, context: Context): Float {
    val resources = context.getResources()
    val metrics = resources.getDisplayMetrics()
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun currencyFormatter(inputValue: Double): String? {
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    currencyFormatter.setMinimumFractionDigits(0)
    val cur = currencyFormatter.format(inputValue.toInt())
    return cur
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
// You could do it as well generic, that's what I do in my lib:

interface SimpleCallback<T> {
    fun callback(data: T)
}


