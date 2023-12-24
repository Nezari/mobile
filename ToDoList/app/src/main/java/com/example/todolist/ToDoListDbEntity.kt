package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_table")
data class ToDoListDbEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var title:String,
    var priority:String
)
