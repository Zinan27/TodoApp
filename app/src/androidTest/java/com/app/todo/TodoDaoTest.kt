package com.app.todo


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.todo.db.AppDatabase
import com.app.todo.db.dao.TodoDao
import com.app.todo.db.entities.TodoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        todoDao = database.todoDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTodo() = runBlocking {
        val todo = TodoEntity(title = "Test Todo", description = "This is a test")
        todoDao.addTodo(todo)

        val todos = todoDao.getAllTodos()
        assertEquals(1, todos.size)
        assertEquals("Test Todo", todos[0].title)
    }

    @Test
    fun updateTodo() = runBlocking {
        val todo = TodoEntity(title = "Initial Title", description = "Initial Description")
        todoDao.addTodo(todo)

        val savedTodo = todoDao.getAllTodos()[0]
        val updatedTodo = savedTodo.copy(title = "Updated Title")
        todoDao.updateTodo(updatedTodo)

        val todos = todoDao.getAllTodos()
        assertEquals(1, todos.size)
        assertEquals("Updated Title", todos[0].title)
    }

    @Test
    fun deleteTodo() = runBlocking {
        val todo = TodoEntity(title = "Delete Test")
        todoDao.addTodo(todo)

        val savedTodo = todoDao.getAllTodos()[0]
        todoDao.deleteTodo(savedTodo)

        val todos = todoDao.getAllTodos()
        assertEquals(0, todos.size)
    }

    @Test
    fun getAllTodos() = runBlocking {
        val todo1 = TodoEntity(title = "Todo 1", description = "Description 1")
        val todo2 = TodoEntity(title = "Todo 2", description = "Description 2")

        todoDao.addTodo(todo1)
        todoDao.addTodo(todo2)

        val todos = todoDao.getAllTodos()
        assertEquals(2, todos.size)
        assertEquals("Todo 1", todos[0].title)
        assertEquals("Todo 2", todos[1].title)
    }
}
