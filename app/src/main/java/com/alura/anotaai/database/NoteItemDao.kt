package com.alura.anotaai.database

import androidx.room.*
import com.alura.anotaai.database.entities.NoteItemEntity

@Dao
interface NoteItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteItem: NoteItemEntity): Long

    @Update
    suspend fun update(noteItem: NoteItemEntity)

    @Delete
    suspend fun delete(noteItem: NoteItemEntity)

    @Query("SELECT * FROM NoteItems WHERE id = :id")
    suspend fun getNoteItemById(id: Long): NoteItemEntity?

    @Query("SELECT * FROM NoteItems WHERE idMainNote = :idMainNote")
    suspend fun getNoteItemByIdMainNote(idMainNote: String): List<NoteItemEntity>

    @Query("SELECT * FROM NoteItems")
    suspend fun getAllNoteItems(): List<NoteItemEntity>
}