package com.app.todo.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.todo.db.AppDatabase
import com.app.todo.db.entities.TodoEntity

@Dao
interface TodoDao {

    @Query("Select * from ${AppDatabase.TODO_TABLE}")
    suspend fun getAllTodos(): List<TodoEntity>

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Insert
    suspend fun addTodo(todoEntity: TodoEntity)


}