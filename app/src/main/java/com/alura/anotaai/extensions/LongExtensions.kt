package com.alura.anotaai.extensions

import java.util.Calendar

fun Long.toDisplayDate(): String {
    val date = this
    val calendar = Calendar.getInstance().apply {
        timeInMillis = date
    }
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)
    return "$day/$month/$year"
}