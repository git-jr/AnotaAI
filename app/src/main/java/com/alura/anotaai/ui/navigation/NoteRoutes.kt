package com.alura.anotaai.ui.navigation

import kotlinx.serialization.Serializable

sealed class NoteRoutes {
    @Serializable
    data object Home : NoteRoutes()

    @Serializable
    data class NoteDetail(val noteId: String? = null) : NoteRoutes()

    @Serializable
    data object Settings : NoteRoutes()
}