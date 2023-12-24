package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Insert
    suspend fun insertTask(entity: Entity)

    @Update
    suspend fun updateTask(entity: Entity)

    @Delete
    suspend fun deleteTask(entity: Entity)

    @Query("DELETE FROM to_do_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM to_do_table")
    suspend fun getTasks():List<CardInfo>
}