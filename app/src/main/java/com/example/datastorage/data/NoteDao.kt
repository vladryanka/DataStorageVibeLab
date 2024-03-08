package com.example.datastorage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll():List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id:Long ):Note

    @Insert
    fun insert(note:String)

    @Update
    fun update(note:Note)

    @Delete
    fun delete(note:Note )

}