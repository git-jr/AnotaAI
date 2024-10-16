package com.alura.anotaai.model

import com.alura.anotaai.database.entities.TextNoteEntity
import java.util.UUID

data class NoteItemText(
    override val id: String = UUID.randomUUID().toString(),
    override val idMainNote: String = "",
    override val date: Long = 0L,
    val content: String,
) : BaseNote(
    id = id,
    idMainNote = idMainNote,
    date = date,
    type = NoteType.TEXT,
) {
    fun toNoteTextEntity(): TextNoteEntity {
        return TextNoteEntity(
            id = id,
            idMainNote = idMainNote,
            date = date,
            content = content
        )
    }
}