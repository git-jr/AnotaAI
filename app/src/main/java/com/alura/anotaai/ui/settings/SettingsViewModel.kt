package com.alura.anotaai.ui.settings

import androidx.lifecycle.ViewModel
import com.alura.anotaai.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    var uiState = _uiState.asStateFlow()

    fun showDeleteDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showConfirmDeleteDialog = show)
    }
}