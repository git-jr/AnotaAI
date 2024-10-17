package com.alura.anotaai.ui.notescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    var uiState = _uiState.asStateFlow()

    fun getNoteById(noteId: String) {
        viewModelScope.launch {
            noteRepository.getNoteById(noteId)?.let {
                _uiState.value = NoteUiState(note = it)
            }
        }
    }

    fun deleteItemNote(noteItem: BaseNote) {
        viewModelScope.launch {
            noteRepository.removeItemNote(noteItem)
            updateCurrentNote()
        }
    }

    private fun updateCurrentNote() {
        viewModelScope.launch {
            _uiState.value.note.id.let {
                getNoteById(it)
            }
        }
    }

    fun updateNoteTextAppBar(text: String) {
        _uiState.value = _uiState.value.copy(noteTextAppBar = text)
    }
}