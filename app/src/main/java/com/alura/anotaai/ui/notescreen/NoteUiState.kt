package com.alura.anotaai.ui.notescreen

import com.alura.anotaai.model.Note

data class NoteUiState(
    val note: Note = Note(),
    val noteTextAppBar: String = "Nova nota ",
    val noteText: String = "",
    val showCameraScreen: Boolean = false,
    val isRecording: Boolean = false,
    val addAudioNote: Boolean = false,
    val audioDuration: Int = 0,
    val audioPath: String = ""
)
