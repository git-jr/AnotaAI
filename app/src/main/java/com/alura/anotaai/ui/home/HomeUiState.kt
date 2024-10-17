package com.alura.anotaai.ui.home

import com.alura.anotaai.model.Note

data class HomeUiState (
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)