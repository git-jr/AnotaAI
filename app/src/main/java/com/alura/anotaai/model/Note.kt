package com.alura.anotaai.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val listItems: List<BaseNote> = emptyList()
)

data class NoteItemText (
    val content: String,
): BaseNote(type = NoteType.TEXT)

data class NoteItemImage (
    val link: String,
): BaseNote(type = NoteType.IMAGE)

data class NoteItemAudio (
    val link: String,
    val duration: Int,
): BaseNote(type = NoteType.AUDIO)

enum class NoteType {
    TEXT,
    IMAGE,
    AUDIO,
}

open class BaseNote(
    val id: String = UUID.randomUUID().toString(),
    val transcription: String = "",
    val type: NoteType = NoteType.TEXT
)
