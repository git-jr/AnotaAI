package com.alura.anotaai.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
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
                _uiState.value = NoteUiState(note = it, noteTextAppBar = it.title)
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            noteRepository.addNote(_uiState.value.note.copy(title = _uiState.value.noteTextAppBar))
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

    fun addNewItemImage(imageLink: String) {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(NoteItemImage(link = imageLink, date = System.currentTimeMillis()))
        _uiState.value = _uiState.value.copy(note = _uiState.value.note.copy(listItems = listItems))
    }

    fun addNewItemAudio() {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(
            NoteItemAudio(
                link = _uiState.value.audioPath,
                duration = _uiState.value.audioDuration,
                date = System.currentTimeMillis()
            )
        )
        _uiState.value = _uiState.value.copy(
            note = _uiState.value.note.copy(listItems = listItems),
            addAudioNote = true
        )
    }

    fun addNewItemText() {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(
            NoteItemText(
                content = _uiState.value.noteText,
                date = System.currentTimeMillis()
            )
        )
        _uiState.value = _uiState.value.copy(
            note = _uiState.value.note.copy(listItems = listItems),
            noteText = ""
        )
    }

    fun updateItemText(newText: String, id: String) {
        val updatedList = _uiState.value.note.listItems.map { item ->
            if (item.id == id && item is NoteItemText) item.copy(content = newText) else item
        }
        _uiState.value =
            _uiState.value.copy(note = _uiState.value.note.copy(listItems = updatedList))
    }

    fun updateNoteText(text: String) {
        _uiState.value = _uiState.value.copy(noteText = text)
    }

    fun updateShowCameraState(show: Boolean) {
        _uiState.value = _uiState.value.copy(showCameraScreen = show)
    }

    fun updateIsRecording(recording: Boolean) {
        _uiState.value = _uiState.value.copy(isRecording = recording)
    }

    fun updateAddAudioNote(add: Boolean) {
        _uiState.value = _uiState.value.copy(addAudioNote = add)
    }

    fun updateAudioDuration(newDuration: Int) {
        _uiState.value = _uiState.value.copy(audioDuration = newDuration)
    }

    fun setAudioPath(audioPath: String) {
        _uiState.value = _uiState.value.copy(audioPath = audioPath)
    }

    fun resetNote() {
        _uiState.value = NoteUiState()
    }
}