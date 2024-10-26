package com.alura.anotaai.ui.home

import com.alura.anotaai.model.Note

data class HomeUiState(
    val notes: List<Note> = emptyList(),
    val showNoteScreen: Boolean = false,
    val itemToDelete: Note? = null
)