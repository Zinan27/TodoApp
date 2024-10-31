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

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                AddNotesScreen(getTodoData())
            }
        }
        addObservers()
    }

    private fun addObservers() {
        mainViewModel.todoState.observe(this) {
            Toast.makeText(this, "Todo added successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getContentType() = intent.getStringExtra(CONTENT_TYPE) ?: TYPE_ADD

    private fun getTodoData(): TodoEntity {
        val todoJson = intent.getStringExtra(TODO_DATA) ?: return TodoEntity()
        val todoEntity = Gson().fromJson(todoJson, TodoEntity::class.java)
        return todoEntity
    }

    @Composable
    private fun AddNotesScreen(todoEntity: TodoEntity) {

        var title by remember { mutableStateOf(todoEntity.title) }
        var description by remember { mutableStateOf(todoEntity.description) }
        var timeStamp by remember { mutableLongStateOf(todoEntity.timeStamp) }
        var icon by remember { mutableIntStateOf(todoEntity.iconType) }

        ConstraintLayout(Modifier.fillMaxSize()) {

            val (saveItem, backButton, heading, titleField, descriptionField, datePickerField, textIcon, iconChoose, deleteBtn) = createRefs()

            BackButton(Modifier.constrainAs(backButton) {
                top.linkTo(heading.top)
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(heading.bottom)
            })

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
                        validateData(todoEntity.id, title, description, timeStamp, icon)
                    }
            )

            HeadingText(
                text = if (getContentType() == TYPE_EDIT) "Edit Task" else "Add Task",
                modifier = Modifier.constrainAs(heading) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 20.dp)
                })

            CustomTextField(
                Modifier
                    .padding(top = 30.dp)
                    .constrainAs(titleField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(heading.bottom)
                    }, "Add Title", title
            ) {
                title = it
            }

            CustomTextField(
                Modifier
                    .padding(top = 10.dp)
                    .constrainAs(descriptionField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(titleField.bottom)
                    }, "Add Description", description
            ) {
                description = it
            }

            DatePickerField(timeStamp, Modifier.constrainAs(datePickerField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(descriptionField.bottom)
            }) {
                timeStamp = it
            }

            SmallHeadingText(
                text = "Choose Icon",
                modifier = Modifier
                    .padding(top = 5.dp)
                    .constrainAs(textIcon) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(datePickerField.bottom)
                    })

            ChooseIconField(
                icon,
                Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .constrainAs(iconChoose) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textIcon.bottom)
                    }) {
                icon = it
            }

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
                            mainViewModel.deleteTodo(todoEntity)
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

    private fun validateData(
        id: Long,
        title: String,
        description: String,
        timeStamp: Long,
        icon: Int
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (timeStamp < System.currentTimeMillis()) {
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show()
            return
        }

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
            mainViewModel.updateTodo(todoEntity)
        } else {
            val todoEntity = TodoEntity(
                title = title,
                description = description,
                date = getDateTimeFromTimestamp(timeStamp),
                timeStamp = timeStamp,
                iconType = icon,
                status = true
            )
            mainViewModel.addTodo(todoEntity)
        }

    }

    companion object {
        const val CONTENT_TYPE = "content_type"
        const val TYPE_EDIT = "edit"
        const val TYPE_ADD = "add"
        const val TODO_DATA = "todo_data"
    }

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    ToDoTheme {
//        //AddNotesScreen()
//    }
//}