package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Insert
    suspend fun insertTask(entity: com.example.todolist.Entity)

    @Update
    suspend fun updateTask(entity: com.example.todolist.Entity)

    @Delete
    suspend fun deleteTask(entity: com.example.todolist.Entity)

    @Query("DELETE FROM to_do_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM to_do_table")
    suspend fun getTasks():List<CardInfo>
}