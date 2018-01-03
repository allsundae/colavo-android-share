package com.colavo.android.utils

import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


fun DateTime.isToday(): Boolean {
    val today = LocalDate.now()
    return this.toLocalDate().equals(today)
}

fun DateTime.isYesterday(): Boolean = this.plusDays(1).isToday()

fun DateTime.toChatTime(): String {
    if(this.isToday())
        return "Today"
    else if(this.isYesterday())
        return "Yesterday"
    else return this.toString("dd MMM", Locale.getDefault())
}

fun ConvertTimestampToDateandTime (timestamp: Long, pattern: String) : String {

    val sfd : SimpleDateFormat = SimpleDateFormat(pattern) //"dd-MM-yyyy HH:mm:ss"
    return sfd.format(Date(timestamp*1000))

}