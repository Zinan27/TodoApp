# Todo App

## Introduction

The **Todo App** is a straightforward Android application designed to help users manage their tasks efficiently. Users can create, update, and delete todos, with features to enhance task management. The app utilizes Room for data persistence and follows modern Android development practices.

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern, which separates the application's logic into three layers:

- **Model**: Represents the data layer, where the app interacts with the Room database.
- **View**: The UI layer that displays the data and receives user input, built with Jetpack Compose.
- **ViewModel**: Acts as a bridge between the Model and View, handling UI-related data in a lifecycle-conscious way.

- 
## Features

- **Todo Management**: Users can add, edit, and delete tasks.
- **QR Code Scanning**: Quickly log tasks by scanning QR codes.
- **Room Database**: Efficient data management and persistence.
- **Responsive UI**: User-friendly interface using Jetpack Compose.

## Dependencies

Include the following dependencies in your `build.gradle` file to support the app's functionality:

```groovy
dependencies {
    implementation "androidx.room:room-runtime:2.4.2"
    kapt "androidx.room:room-compiler:2.4.2"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0"
    implementation "androidx.camera:camera-core:1.2.2"
    implementation "androidx.camera:camera-camera2:1.2.2"
    implementation "androidx.camera:camera-view:1.2.2"
    implementation "com.google.accompanist:accompanist-permissions:0.27.0"
    // Other dependencies as needed
}
```


## Code Snippets

### TodoEntity.kt

This data class represents a Todo entity in the Room database.

```kotlin
@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)
```
### TodoDao.kt

This interface defines the Data Access Object (DAO) for managing Todo entities.

```kotlin

@Dao
interface TodoDao {
    @Insert
    suspend fun addTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoEntity>
}
```

## Testing

TodoDaoTest
Testing is essential to ensure that the application's data layer is functioning correctly. The following unit tests verify the CRUD operations of the TodoDao.

```kotlin

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun setup() {
        // Create an in-memory database for testing
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
        // Test inserting a todo
        val todo = TodoEntity(title = "Test Todo", description = "This is a test")
        todoDao.addTodo(todo)
        val todos = todoDao.getAllTodos()
        assertEquals(1, todos.size)
        assertEquals("Test Todo", todos[0].title)
    }
    
    @Test
    fun deleteTodo() = runBlocking {
        // Test deleting a todo
        val todo = TodoEntity(title = "Delete Test")
        todoDao.addTodo(todo)
        todoDao.deleteTodo(todo)
        val todos = todoDao.getAllTodos()
        assertEquals(0, todos.size)
    }
}
```
# Sideloading the App

This guide explains how to sideload the application onto your Android device using Android Studio.

## Prerequisites

1. **Android Device**: Make sure you have an Android device with USB debugging enabled.
2. **Android Studio**: Ensure you have [Android Studio](https://developer.android.com/studio) installed on your machine.

## Steps to Sideload the App

. Enable USB Debugging on Your Device

- Go to **Settings** on your Android device.
- Scroll down and tap **About phone**.
- Find the **Build number** and tap it **7 times** to enable Developer options.
- Go back to **Settings** and tap **Developer options**.
- Enable **USB debugging**.

### 2. Connect Your Device to Your Computer

- Use a USB cable to connect your Android device to your computer.
- When prompted, select **File Transfer (MTP)** mode.

### 3. Open the Project in Android Studio

- Launch **Android Studio**.
- Open the project that you want to sideload.

### 4. Device will show in Android studio 

- At the top the device name should show next to the run button

### 5. Run the app

- Run the app
  




