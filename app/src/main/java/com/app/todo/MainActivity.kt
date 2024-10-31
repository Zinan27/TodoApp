package com.app.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.ui.theme.ToDoTheme

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
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (addImg, heading, list) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add button",
            modifier = Modifier
                .size(40.dp)
                .constrainAs(addImg) {
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        )

        HeadingText(text = "Todo List", modifier = Modifier.constrainAs(heading) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top, margin = 20.dp)
        })

        TodoList(modifier = Modifier.constrainAs(list) {
            top.linkTo(heading.bottom, margin = 50.dp)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

    }
}

@Composable
private fun TodoList(modifier: Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(100) {
            TodoItem()
        }
    }
}

@Composable
fun TodoItem() {

    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        val (headingText, descriptionText, icon, optionsRow) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.work),
            contentDescription = "Icon",
            modifier = Modifier
                .size(20.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        SmallHeadingText(text = "This is title", modifier = Modifier.constrainAs(headingText) {
            top.linkTo(icon.top)
            start.linkTo(icon.end, margin = 5.dp)
        })

        DescriptionText(text = "10 Oct", modifier = Modifier.constrainAs(descriptionText) {
            top.linkTo(headingText.bottom, margin = 2.dp)
            start.linkTo(icon.start)

        })

        OptionsRow(modifier = Modifier
            .padding(horizontal = 16.dp)
            .constrainAs(optionsRow) {
                top.linkTo(descriptionText.bottom, 3.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            })
    }

}

@Composable
fun OptionsRow(modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.edit),
            contentDescription = "Edit button",
            modifier = Modifier
                .size(20.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete button",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.share),
            contentDescription = "Share button",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoTheme {
        MainScreen()
    }
}