package com.alura.anotaai.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val listMedias: List<BaseMedia> = emptyList()
)

data class Audio(
    val duration: Int,
) : BaseMedia(type = MediaType.AUDIO)

data class Image(
    val width: Int,
    val height: Int,
) : BaseMedia(type = MediaType.IMAGE)

open class BaseMedia(
    val id: String = UUID.randomUUID().toString(),
    val path: String = "",
    val transcription: String = "",
    val type: MediaType = MediaType.IMAGE
)

// criar o MediaType como enum de tipos de mídia
enum class MediaType {
    IMAGE,
    AUDIO,
}


val sampleNotes = listOf(
    Note(
        title = "Note 1",
        description = "Description 1",
        listMedias = listOf(
            Audio(42),
            Image(420, 420),
        ),
    ),
    Note(
        title = "Note 2",
        description = "Description 2",
        listMedias = listOf(
            Image(69, 69),
        ),
    ),
)