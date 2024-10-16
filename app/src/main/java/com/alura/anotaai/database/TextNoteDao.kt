package com.alura.anotaai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.alura.anotaai.database.entities.TextNoteEntity

@Dao
interface TextNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: TextNoteEntity): Long

    @Query("SELECT * FROM TextNotes WHERE idMainNote = :idMainNote")
    suspend fun getByIdMainNote(idMainNote: String): List<TextNoteEntity>

    @Query("SELECT * FROM TextNotes WHERE id = :id")
    suspend fun getNoteById(id: String): TextNoteEntity?

    @Query("SELECT * FROM TextNotes")
    suspend fun getAllNotes(): List<TextNoteEntity>

    @Update
    suspend fun update(note: TextNoteEntity)

    @Delete
    suspend fun delete(note: TextNoteEntity)
}