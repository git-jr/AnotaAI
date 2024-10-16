package com.alura.anotaai.repository

import com.alura.anotaai.database.AudioNoteDao
import com.alura.anotaai.database.ImageNoteDao
import com.alura.anotaai.database.NoteDao
import com.alura.anotaai.database.TextNoteDao
import com.alura.anotaai.database.entities.toNoteItemAudio
import com.alura.anotaai.database.entities.toNoteItemImage
import com.alura.anotaai.database.entities.toNoteItemText
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import javax.inject.Inject


class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val textNoteDao: TextNoteDao,
    private val imageNoteDao: ImageNoteDao,
    private val audioNoteDao: AudioNoteDao,
) {
    suspend fun insert(note: Note) {
        noteDao.insert(note.toNoteEntity())
        note.listItems.forEach { noteItem ->
            when (noteItem) {
                is NoteItemText -> textNoteDao.insert(
                    noteItem.toNoteTextEntity().copy(idMainNote = note.id)
                )

                is NoteItemImage -> imageNoteDao.insert(
                    noteItem.toNoteImageEntity().copy(idMainNote = note.id)
                )

                is NoteItemAudio -> audioNoteDao.insert(
                    noteItem.toAudioNoteEntity().copy(idMainNote = note.id)
                )
            }
        }
    }

    suspend fun getAllNotes(): List<Note> {
        val allNotes = noteDao.getAllNotes()
        return allNotes.map { noteEntity ->
            val textNotes = textNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemText() }
            val imageNotes =
                imageNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemImage() }
            val audioNotes =
                audioNoteDao.getByIdMainNote(noteEntity.id).map { it.toNoteItemAudio() }
            Note(
                id = noteEntity.id,
                title = noteEntity.title,
                listItems = textNotes + imageNotes + audioNotes
            )
        }
    }
}
