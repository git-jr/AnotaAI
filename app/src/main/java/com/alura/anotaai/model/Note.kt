package com.alura.anotaai.model

import com.alura.anotaai.database.entities.NoteEntity
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val listItems: List<BaseNote> = emptyList()
) {
    fun toNoteEntity() = NoteEntity(
        id = id,
        title = title
    )
}


