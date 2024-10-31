package com.app.todo.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.presentation.BackButton
import com.app.todo.presentation.ChooseIconField
import com.app.todo.presentation.CustomTextField
import com.app.todo.presentation.DatePickerField
import com.app.todo.presentation.HeadingText
import com.app.todo.presentation.SmallHeadingText
import com.app.todo.presentation.screens.ui.theme.PrimaryColor
import com.app.todo.presentation.screens.ui.theme.ToDoTheme

class AddNotesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                AddNotesScreen()
            }
        }
    }

    private fun getContentType() = intent.getStringExtra(CONTENT_TYPE) ?: TYPE_ADD

    @Composable
    private fun AddNotesScreen() {
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
            )

            HeadingText(text = "Add Task", modifier = Modifier.constrainAs(heading) {
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
                    }, "Add Title"
            )

            CustomTextField(
                Modifier
                    .padding(top = 10.dp)
                    .constrainAs(descriptionField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(titleField.bottom)
                    }, "Add Description"
            )

            DatePickerField(Modifier.constrainAs(datePickerField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(descriptionField.bottom)
            })

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
                Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .constrainAs(iconChoose) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textIcon.bottom)
                    })

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
                ) {
                    HeadingText(
                        text = "Delete",
                        textColor = Color.White
                    )
                }
            }


        }
    }

    companion object {
        const val CONTENT_TYPE = "content_type"
        const val TYPE_EDIT = "edit"
        const val TYPE_ADD = "add"
    }

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    ToDoTheme {
//        //AddNotesScreen()
//    }
//}