package com.example.findmovie.presentation.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateFormat(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    return format.format(date)
}