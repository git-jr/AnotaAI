package com.alura.anotaai.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alura.anotaai.model.Note
import java.util.UUID

@Entity(tableName = "Notes")
data class NoteEntity(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: Long = System.currentTimeMillis(),
    val thumbnail: String? = null
)

fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    date = date,
    thumbnail = thumbnail
)