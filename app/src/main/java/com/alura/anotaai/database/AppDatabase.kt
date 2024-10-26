package com.alura.anotaai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alura.anotaai.database.entities.AudioNoteEntity
import com.alura.anotaai.database.entities.ImageNoteEntity
import com.alura.anotaai.database.entities.NoteEntity
import com.alura.anotaai.database.entities.TextNoteEntity


@Database(
    entities = [NoteEntity::class,
        TextNoteEntity::class,
        AudioNoteEntity::class,
        ImageNoteEntity::class
    ],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun textNoteDao(): TextNoteDao
    abstract fun audioNoteDao(): AudioNoteDao
    abstract fun imageNoteDao(): ImageNoteDao
}
