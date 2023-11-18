package com.example.todolist

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "todolist_table", indices = [Index("id")])
data class ToDoListDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "description") val descCol: String,
    @ColumnInfo(name = "priority") val priorCol: Byte
)
