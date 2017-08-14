package com.colavo.android.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by RUS on 11.07.2016.
 */

fun Context.toast(message: CharSequence, length: Int = Toast.LENGTH_LONG) = Toast.makeText(this, message, length).show()