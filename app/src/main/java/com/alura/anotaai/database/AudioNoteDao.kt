package com.alura.anotaai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alura.anotaai.database.entities.AudioNoteEntity

@Dao
interface AudioNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audioNote: AudioNoteEntity): Long

    @Update
    suspend fun update(audioNote: AudioNoteEntity)

    @Query("DELETE FROM AudioNotes WHERE id = :itemID")
    suspend fun delete(itemID: String)

    @Query("SELECT * FROM AudioNotes WHERE idMainNote = :idMainNote")
    suspend fun getByIdMainNote(idMainNote: String): List<AudioNoteEntity>

    @Query("SELECT * FROM AudioNotes WHERE id = :id")
    suspend fun getAudioNoteById(id: String): AudioNoteEntity?

    @Query("DELETE FROM AudioNotes WHERE id = :idMainNote")
    suspend fun deleteByIdMainNote(idMainNote: String)
}