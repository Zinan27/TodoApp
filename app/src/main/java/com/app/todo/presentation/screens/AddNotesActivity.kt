package com.app.todo.presentation.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.db.entities.TodoEntity
import com.app.todo.presentation.BackButton
import com.app.todo.presentation.ChooseIconField
import com.app.todo.presentation.CustomTextField
import com.app.todo.presentation.DatePickerField
import com.app.todo.presentation.HeadingText
import com.app.todo.presentation.SmallHeadingText
import com.app.todo.presentation.screens.ui.theme.PrimaryColor
import com.app.todo.presentation.screens.ui.theme.ToDoTheme
import com.app.todo.presentation.viewmodels.MainViewModel
import com.app.todo.utils.getDateTimeFromTimestamp
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNotesActivity : ComponentActivity() {

    // ViewModel to manage UI-related data for the activity
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using Jetpack Compose
        setContent {
            ToDoTheme {
                // Display the AddNotesScreen with the retrieved TodoEntity data
                AddNotesScreen(getTodoData())
            }
        }
        // Observe changes in the ViewModel's todoState
        addObservers()
    }

    private fun addObservers() {
        // Show a Toast message when a todo item is added
        mainViewModel.todoState.observe(this) {
            Toast.makeText(this, "Todo added successfully", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        }
    }

    // Retrieve the content type from the intent
    private fun getContentType() = intent.getStringExtra(CONTENT_TYPE) ?: TYPE_ADD

    // Retrieve TodoEntity data from the intent as a JSON string
    private fun getTodoData(): TodoEntity {
        val todoJson = intent.getStringExtra(TODO_DATA) ?: return TodoEntity()
        // Deserialize the JSON string into a TodoEntity object
        return Gson().fromJson(todoJson, TodoEntity::class.java)
    }

    @Composable
    private fun AddNotesScreen(todoEntity: TodoEntity) {
        // Manage state for title, description, timestamp, and icon using remember
        var title by remember { mutableStateOf(todoEntity.title) }
        var description by remember { mutableStateOf(todoEntity.description) }
        var timeStamp by remember { mutableLongStateOf(todoEntity.timeStamp) }
        var icon by remember { mutableIntStateOf(todoEntity.iconType) }

        // Use ConstraintLayout for arranging UI components
        ConstraintLayout(Modifier.fillMaxSize()) {
            // Create references for the UI elements
            val (saveItem, backButton, heading, titleField, descriptionField, datePickerField, textIcon, iconChoose, deleteBtn) = createRefs()

            // Back button
            BackButton(Modifier.constrainAs(backButton) {
                top.linkTo(heading.top)
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(heading.bottom)
            })

            // Save button
            SmallHeadingText(
                text = "Save",
                textColor = PrimaryColor,
                modifier = Modifier
                    .constrainAs(saveItem) {
                        top.linkTo(heading.top)
                        end.linkTo(parent.end, margin = 16.dp)
                        bottom.linkTo(heading.bottom)
                    }
                    .clickable {
                        // Validate input data before saving
                        validateData(todoEntity.id, title, description, timeStamp, icon)
                    }
            )

            // Heading text
            HeadingText(
                text = if (getContentType() == TYPE_EDIT) "Edit Task" else "Add Task",
                modifier = Modifier.constrainAs(heading) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 20.dp)
                })

            // Title input field
            CustomTextField(
                Modifier
                    .padding(top = 30.dp)
                    .constrainAs(titleField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(heading.bottom)
                    }, "Add Title", title
            ) {
                title = it // Update title state
            }

            // Description input field
            CustomTextField(
                Modifier
                    .padding(top = 10.dp)
                    .constrainAs(descriptionField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(titleField.bottom)
                    }, "Add Description", description
            ) {
                description = it // Update description state
            }

            // Date picker field
            DatePickerField(timeStamp, Modifier.constrainAs(datePickerField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(descriptionField.bottom)
            }) {
                timeStamp = it // Update timestamp state
            }

            // Icon selection text
            SmallHeadingText(
                text = "Choose Icon",
                modifier = Modifier
                    .padding(top = 5.dp)
                    .constrainAs(textIcon) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(datePickerField.bottom)
                    })

            // Icon selection field
            ChooseIconField(
                icon,
                Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .constrainAs(iconChoose) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textIcon.bottom)
                    }) {
                icon = it // Update icon state
            }

            // Delete button visible only when editing a task
            if (getContentType() == TYPE_EDIT) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 80.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(
                            Color.Red,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .constrainAs(deleteBtn) {
                            top.linkTo(iconChoose.bottom, margin = 50.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start)
                        }
                        .clickable {
                            mainViewModel.deleteTodo(todoEntity) // Delete the todo item
                        }
                ) {
                    HeadingText(
                        text = "Delete",
                        textColor = Color.White
                    )
                }
            }
        }
    }

    // Validate the input data before saving
    private fun validateData(
        id: Long,
        title: String,
        description: String,
        timeStamp: Long,
        icon: Int
    ) {
        // Check if title and description are not empty
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the timestamp is valid
        if (timeStamp < System.currentTimeMillis()) {
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show()
            return
        }

        // Create or update TodoEntity based on content type
        if (getContentType() == TYPE_EDIT) {
            val todoEntity = TodoEntity(
                id,
                title,
                description,
                getDateTimeFromTimestamp(timeStamp),
                timeStamp,
                icon,
                true
            )
            mainViewModel.updateTodo(todoEntity) // Update the todo item
        } else {
            val todoEntity = TodoEntity(
                title = title,
                description = description,
                date = getDateTimeFromTimestamp(timeStamp),
                timeStamp = timeStamp,
                iconType = icon,
                status = true
            )
            mainViewModel.addTodo(todoEntity) // Add a new todo item
        }
    }

    companion object {
        const val CONTENT_TYPE = "content_type" // Key for content type in the intent
        const val TYPE_EDIT = "edit" // Type for editing a todo
        const val TYPE_ADD = "add" // Type for adding a todo
        const val TODO_DATA = "todo_data" // Key for todo data in the intent
    }
}

// Uncomment to preview the composable function
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    ToDoTheme {
//        //AddNotesScreen()
//    }
//}
