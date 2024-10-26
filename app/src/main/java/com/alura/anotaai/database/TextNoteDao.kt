package com.alura.anotaai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alura.anotaai.database.entities.TextNoteEntity

@Dao
interface TextNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: TextNoteEntity): Long

    @Query("SELECT * FROM TextNotes WHERE idMainNote = :idMainNote")
    suspend fun getByIdMainNote(idMainNote: String): List<TextNoteEntity>

    @Query("SELECT * FROM TextNotes WHERE id = :id")
    suspend fun getNoteById(id: String): TextNoteEntity?

    @Update
    suspend fun update(note: TextNoteEntity)

    @Query("DELETE FROM TextNotes WHERE id = :itemID")
    suspend fun delete(itemID: String)

    @Query("DELETE FROM TextNotes WHERE idMainNote = :idMainNote")
    suspend fun deleteByIdMainNote(idMainNote: String)

    @Query("DELETE FROM TextNotes")
    suspend fun deleteAll()
}