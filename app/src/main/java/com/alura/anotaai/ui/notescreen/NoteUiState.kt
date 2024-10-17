package com.alura.anotaai.ui.notescreen

import com.alura.anotaai.model.Note

data class NoteUiState(
    val note: Note = Note(),
    val noteTextAppBar: String = "Nova nota ",
)
