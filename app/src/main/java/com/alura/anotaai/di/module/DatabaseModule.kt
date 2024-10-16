package com.alura.anotaai.di.module

import android.content.Context
import androidx.room.Room
import com.alura.anotaai.database.AppDatabase
import com.alura.anotaai.database.NoteDao
import com.alura.anotaai.database.NoteItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "anotaai.db"

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset("database/$DATABASE_NAME")
            .build()
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    fun provideNoteItemDao(db: AppDatabase): NoteItemDao {
        return db.noteItemDao()
    }
}