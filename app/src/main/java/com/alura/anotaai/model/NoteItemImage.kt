package com.alura.anotaai.model

import com.alura.anotaai.database.entities.ImageNoteEntity
import java.util.UUID

data class NoteItemImage(
    override val id: String = UUID.randomUUID().toString(),
    override val idMainNote: String = "",
    override val date: Long = 0L,
    val link: String,
) : BaseNote(
    id = id,
    idMainNote = idMainNote,
    date = date,
    type = NoteType.IMAGE
) {
    fun toNoteImageEntity(): ImageNoteEntity {
        return ImageNoteEntity(
            id = id,
            idMainNote = idMainNote,
            date = date,
            link = link
        )
    }
}