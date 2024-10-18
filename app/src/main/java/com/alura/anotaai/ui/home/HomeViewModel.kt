package com.alura.anotaai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.anotaai.model.Note
import com.alura.anotaai.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    var uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllNotes()
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            noteRepository.removeNote(note)
            getAllNotes()
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNote(note).let {
                getAllNotes()
            }
        }
    }

    private suspend fun getAllNotes() {
        noteRepository.getAllNotes().let {
            _uiState.value = HomeUiState(notes = it)
        }
    }

    fun setNoteToEdit(noteId: String?, showNoteScreen: Boolean = false) {
        _uiState.value = _uiState.value.copy(idEditNote = noteId, showNoteScreen = showNoteScreen)
    }
}