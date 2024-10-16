package com.alura.anotaai.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alura.anotaai.model.Note

@Entity(tableName = "Notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val title: String,
)

fun NoteEntity.toNote() = Note(
    id = id.toString(),
    title = title,
)