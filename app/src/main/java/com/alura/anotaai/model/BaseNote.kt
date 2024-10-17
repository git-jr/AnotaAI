package com.alura.anotaai.model

import java.util.UUID

open class BaseNote(
    open val id: String = UUID.randomUUID().toString(),
    open val idMainNote: String = "",
    open val date: Long = 0L,
    val type: NoteType = NoteType.TEXT,
)

enum class NoteType {
    TEXT,
    IMAGE,
    AUDIO,
}