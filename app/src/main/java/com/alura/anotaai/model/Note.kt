package com.alura.anotaai.model

import com.alura.anotaai.database.entities.NoteEntity
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val date: Long = System.currentTimeMillis(),
    val listItems: List<BaseNote> = emptyList(),
    val thumbnail: String? = null
) {
    fun toNoteEntity(): NoteEntity {
        var thumbnail: String = NoteType.TEXT.name
        if (this.listItems.isNotEmpty()) {
            thumbnail = when (this.listItems.last().type) {
                NoteType.TEXT -> NoteType.TEXT.name
                NoteType.IMAGE -> (this.listItems.last() as NoteItemImage).link
                NoteType.AUDIO -> NoteType.AUDIO.name
            }
        }

        return NoteEntity(
            id = id,
            title = title,
            date = date,
            thumbnail = thumbnail
        )
    }
}


