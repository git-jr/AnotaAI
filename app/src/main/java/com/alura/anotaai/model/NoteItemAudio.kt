package com.alura.anotaai.model

import com.alura.anotaai.database.entities.AudioNoteEntity
import java.util.UUID

data class NoteItemAudio(
    override val id: String = UUID.randomUUID().toString(),
    override val idMainNote: String = "",
    override val date: Long = 0L,
    val link: String,
    val duration: Int,
) : BaseNote(
    id = id,
    idMainNote = idMainNote,
    date = date,
    type = NoteType.AUDIO
) {
    fun toAudioNoteEntity(): AudioNoteEntity {
        return AudioNoteEntity(
            id = id,
            idMainNote = idMainNote,
            date = date,
            link = link,
            duration = duration
        )
    }
}