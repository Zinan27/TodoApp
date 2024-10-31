package com.app.todo.presentation.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.R
import com.app.todo.presentation.CustomSearchBar
import com.app.todo.presentation.HeadingText
import com.app.todo.presentation.TodoItem
import com.app.todo.presentation.screens.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (import, addImg, heading, searchBar, list) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add button",
            modifier = Modifier
                .size(30.dp)
                .constrainAs(addImg) {
                    top.linkTo(heading.top)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(heading.bottom)
                }
                .clickable {
                    context.startActivity(Intent(context, AddNotesActivity::class.java).also {
                        it.putExtra(AddNotesActivity.CONTENT_TYPE, AddNotesActivity.TYPE_ADD)
                    })
                }
        )

        HeadingText(text = "Todo List", modifier = Modifier.constrainAs(heading) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top, margin = 20.dp)
        })

        Image(
            painter = painterResource(id = R.drawable.importfile),
            contentDescription = "Add button",
            modifier = Modifier
                .size(30.dp)
                .constrainAs(import) {
                    top.linkTo(heading.top)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(heading.bottom)
                }
                .clickable {
                    context.startActivity(Intent(context, AddNotesActivity::class.java).also {
                        it.putExtra(AddNotesActivity.CONTENT_TYPE, AddNotesActivity.TYPE_ADD)
                    })
                }
        )

        CustomSearchBar(Modifier.constrainAs(searchBar) {
            top.linkTo(heading.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        TodoList(modifier = Modifier
            .padding(top = 50.dp)
            .constrainAs(list) {
                top.linkTo(searchBar.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

    }
}


@Composable
private fun TodoList(modifier: Modifier) {
    val context = LocalContext.current
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(100) {
            TodoItem {
                context.startActivity(Intent(context, AddNotesActivity::class.java).also {
                    it.putExtra(AddNotesActivity.CONTENT_TYPE, AddNotesActivity.TYPE_EDIT)
                })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoTheme {
        MainScreen()
    }
}