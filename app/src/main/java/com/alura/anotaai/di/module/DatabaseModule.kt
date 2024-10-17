package com.alura.anotaai.di.module

import android.content.Context
import androidx.room.Room
import com.alura.anotaai.database.AppDatabase
import com.alura.anotaai.database.AudioNoteDao
import com.alura.anotaai.database.ImageNoteDao
import com.alura.anotaai.database.NoteDao
import com.alura.anotaai.database.TextNoteDao
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
        ).build()
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    fun provideTextNoteDao(db: AppDatabase): TextNoteDao {
        return db.textNoteDao()
    }

    @Provides
    fun provideAudioNoteDao(db: AppDatabase): AudioNoteDao {
        return db.audioNoteDao()
    }

    @Provides
    fun provideImageNoteDao(db: AppDatabase): ImageNoteDao {
        return db.imageNoteDao()
    }
}