package com.alura.anotaai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alura.anotaai.database.entities.ImageNoteEntity

@Dao
interface ImageNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageNote: ImageNoteEntity): Long

    @Query("SELECT * FROM ImageNotes WHERE idMainNote = :idMainNote")
    suspend fun getByIdMainNote(idMainNote: String): List<ImageNoteEntity>

    @Query("SELECT * FROM ImageNotes WHERE id = :id")
    suspend fun getImageNoteById(id: String): ImageNoteEntity?

    @Update
    suspend fun update(imageNote: ImageNoteEntity)

    @Query("DELETE FROM ImageNotes WHERE id = :itemID")
    suspend fun delete(itemID: String)

    @Query("DELETE FROM ImageNotes WHERE id = :idMainNote")
    suspend fun deleteByIdMainNote(idMainNote: String)

    @Query("DELETE FROM ImageNotes")
    suspend fun deleteAll()
}