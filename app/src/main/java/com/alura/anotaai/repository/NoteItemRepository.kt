package com.alura.anotaai.repository

import com.alura.anotaai.database.AudioNoteDao
import com.alura.anotaai.database.ImageNoteDao
import com.alura.anotaai.database.TextNoteDao
import javax.inject.Inject


class NoteItemRepository @Inject constructor(
    private val textNoteDao: TextNoteDao,
    private val imageNoteDao: ImageNoteDao,
    private val audioNoteDao: AudioNoteDao,
) {
}