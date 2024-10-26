package com.alura.anotaai.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.anotaai.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    var uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes().collectLatest { notes ->
                _uiState.value = _uiState.value.copy(notesCount = notes.size)
            }
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showConfirmDeleteDialog = show)
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }
}