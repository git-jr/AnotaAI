package com.alura.anotaai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alura.anotaai.database.entities.NoteEntity
import com.alura.anotaai.database.entities.NoteItemEntity


@Database(
    entities = [NoteEntity::class, NoteItemEntity::class],
    version = 1,
    exportSchema = true,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun noteItemDao(): NoteItemDao
}
