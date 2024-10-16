package com.alura.anotaai.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import java.util.UUID


@Entity(tableName = "AudioNotes")
data class AudioNoteEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val idMainNote: String = "",
    val date: Long = 0L,
    val link: String = "",
    val duration: Int = 0
)

fun AudioNoteEntity.toNoteItemAudio(): NoteItemAudio {
    return NoteItemAudio(
        id = id,
        idMainNote = idMainNote,
        date = date,
        link = link,
        duration = duration
    )
}
