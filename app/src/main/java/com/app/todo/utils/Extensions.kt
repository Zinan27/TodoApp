package com.app.todo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getDateTimeFromTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("EEEE, MMM. dd, yyyy hh:mm a", Locale.ENGLISH)
    val date = Date(timestamp)
    return formatter.format(date)
}