package com.alura.anotaai.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteType
import java.util.UUID

@Entity(tableName = "NoteItems")
data class NoteItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val idMainNote: String = "",
    val transcription: String = "",
    val type: String = "",
)

fun NoteItemEntity.toBaseNote() = BaseNote(
    id = id.toString(),
    transcription = transcription,
    type = NoteType.valueOf(type),
    idMainNote = idMainNote
)
