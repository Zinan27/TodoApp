package com.app.todo.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.todo.db.AppDatabase

@Entity(tableName = AppDatabase.TODO_TABLE)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val iconType: Int = 1,
    val status: Boolean = true
)
