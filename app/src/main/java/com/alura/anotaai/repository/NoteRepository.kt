package com.alura.anotaai.repository

import com.alura.anotaai.database.AudioNoteDao
import com.alura.anotaai.database.ImageNoteDao
import com.alura.anotaai.database.NoteDao
import com.alura.anotaai.database.TextNoteDao
import com.alura.anotaai.database.entities.toNote
import com.alura.anotaai.database.entities.toNoteItemAudio
import com.alura.anotaai.database.entities.toNoteItemImage
import com.alura.anotaai.database.entities.toNoteItemText
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val textNoteDao: TextNoteDao,
    private val imageNoteDao: ImageNoteDao,
    private val audioNoteDao: AudioNoteDao,
) {

    suspend fun addNote(note: Note) {
        noteDao.insert(note.toNoteEntity())
        note.listItems.forEach { noteItem ->
            val currentTime = System.currentTimeMillis()
            when (noteItem) {
                is NoteItemText -> textNoteDao.insert(
                    noteItem.toNoteTextEntity().copy(idMainNote = note.id, date = currentTime)
                )

                is NoteItemImage -> imageNoteDao.insert(
                    noteItem.toNoteImageEntity().copy(idMainNote = note.id, date = currentTime)
                )

                is NoteItemAudio -> audioNoteDao.insert(
                    noteItem.toAudioNoteEntity().copy(idMainNote = note.id, date = currentTime)
                )
            }
        }
    }

    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
            .map { noteList -> noteList.map { it.toNote() }.sortedByDescending { it.date } }
    }

    suspend fun getNoteById(noteId: String): Note? {
        val noteEntity = noteDao.getNoteById(noteId) ?: return null
        val textNotes = textNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemText() }
        val imageNotes = imageNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemImage() }
        val audioNotes = audioNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemAudio() }
        return Note(
            id = noteEntity.id,
            title = noteEntity.title,
            listItems = textNotes + imageNotes + audioNotes
        )
    }

    suspend fun removeNote(note: Note) {
        noteDao.delete(note.toNoteEntity())

        textNoteDao.deleteByIdMainNote(note.id)
        imageNoteDao.deleteByIdMainNote(note.id)
        audioNoteDao.deleteByIdMainNote(note.id)
    }

    suspend fun removeItemNote(noteItem: BaseNote) {
        when (noteItem) {
            is NoteItemText -> textNoteDao.delete(noteItem.id)
            is NoteItemImage -> imageNoteDao.delete(noteItem.id)
            is NoteItemAudio -> audioNoteDao.delete(noteItem.id)
        }
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAll()
        textNoteDao.deleteAll()
        imageNoteDao.deleteAll()
        audioNoteDao.deleteAll()
    }

}


