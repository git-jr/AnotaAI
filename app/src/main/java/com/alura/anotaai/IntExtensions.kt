package com.alura.anotaai

import java.util.Locale

fun Int.audioDisplay(): String {
    return String.format(Locale.getDefault(), "%02d:%02d", this / 60, this % 60)
}