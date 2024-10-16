package com.alura.anotaai.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alura.anotaai.model.NoteItemImage
import java.util.UUID


@Entity(tableName = "ImageNotes")
data class ImageNoteEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val idMainNote: String = "",
    val date: Long = 0L,
    val link: String = ""
)

fun ImageNoteEntity.toNoteItemImage(): NoteItemImage {
    return NoteItemImage(
        id = id,
        idMainNote = idMainNote,
        date = date,
        link = link
    )
}