package com.app.todo.presentation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.R
import com.app.todo.db.entities.TodoEntity
import com.app.todo.presentation.screens.QRGeneratorActivity
import com.app.todo.presentation.screens.ui.theme.LightBlue
import com.app.todo.presentation.screens.ui.theme.PrimaryColor
import com.app.todo.utils.getDateTimeFromTimestamp
import com.google.gson.Gson

@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        color = textColor
    )
}

@Composable
fun SmallHeadingText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = textColor
    )
}

@Composable
fun BackButton(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Back Button",
        modifier.size(25.dp)
    )
}

@Composable
fun DescriptionText(text: String, modifier: Modifier = Modifier, textColor: Color = Color.Black) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        color = textColor
    )
}

@Composable
fun CustomSearchBar(modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp)),
        placeholder = {
            Text(text = stringResource(R.string.search))
        }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            disabledTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            disabledIndicatorColor = Color.White,
        )
    )
}


@Composable
fun CustomTextField(
    modifier: Modifier,
    hint: String = "",
    initialValue: String = "",
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    TextField(
        maxLines = 2,
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        placeholder = { Text(text = hint) },
        modifier = modifier
            .height(65.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, PrimaryColor, RoundedCornerShape(20.dp)),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )

}

@Composable
fun TodoItem(todoEntity: TodoEntity, callBack: () -> Unit = {}) {

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .background(
                color = LightBlue,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                callBack()
            }
    ) {
        val (headingText, descriptionText, icon, indicator, optionsRow) = createRefs()

        val iconDisplay = getIcon(todoEntity.iconType)

        Image(
            painter = painterResource(id = iconDisplay),
            contentDescription = "Icon",
            modifier = Modifier
                .size(20.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        Box(modifier = Modifier
            .padding(10.dp)
            .size(15.dp)
            .background(
                if (todoEntity.timeStamp < System.currentTimeMillis()) Color.Red else Color.Green,
                shape = CircleShape
            )
            .constrainAs(indicator) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            })

        SmallHeadingText(text = todoEntity.title, modifier = Modifier.constrainAs(headingText) {
            top.linkTo(icon.top)
            start.linkTo(icon.end, margin = 5.dp)
        })

        DescriptionText(text = todoEntity.date, modifier = Modifier.constrainAs(descriptionText) {
            top.linkTo(headingText.bottom, margin = 2.dp)
            start.linkTo(icon.start)

        })

        Image(
            painter = painterResource(id = R.drawable.share),
            contentDescription = "Share button",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
                .constrainAs(optionsRow) {
                    top.linkTo(descriptionText.bottom, 3.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .clickable {
                    context.startActivity(Intent(context, QRGeneratorActivity::class.java).also {
                        it.putExtra(QRGeneratorActivity.TODO_DATA, Gson().toJson(todoEntity))
                    })
                }
        )

    }

}

fun getIcon(iconNum: Int): Int {
    return when (iconNum) {
        1 -> R.drawable.work
        2 -> R.drawable.medicine
        3 -> R.drawable.education
        4 -> R.drawable.exercise
        5 -> R.drawable.food
        6 -> R.drawable.prayer
        else -> R.drawable.work
    }
}

@Composable
fun OptionsRow(modifier: Modifier, share: () -> Unit) {
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
                .clickable {
                    share()
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ChooseIconField(selectedIcon: Int, modifier: Modifier, callBack: (Int) -> Unit) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconImage(icon = R.drawable.work, selectedIcon == 1) {
            callBack(1)
        }
        IconImage(icon = R.drawable.medicine, selectedIcon == 2) {
            callBack(2)
        }
        IconImage(icon = R.drawable.education, selectedIcon == 3) {
            callBack(3)
        }
        IconImage(icon = R.drawable.exercise, selectedIcon == 4) {
            callBack(4)
        }
        IconImage(icon = R.drawable.food, selectedIcon == 5) {
            callBack(5)
        }
        IconImage(icon = R.drawable.prayer, selectedIcon == 6) {
            callBack(6)
        }
    }
}

@Composable
fun IconImage(icon: Int, isSelected: Boolean, callBack: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = "Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    callBack()
                }
        )

        if (isSelected) {
            Image(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Work Icon",
                modifier = Modifier.size(20.dp)
            )
        }
    }

}


@Composable
fun DatePickerField(timeStamp: Long, modifier: Modifier, callBack: (Long) -> Unit) {

    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(65.dp)
            .fillMaxWidth()
            .border(1.dp, PrimaryColor, RoundedCornerShape(20.dp))
            .clickable {
                showDatePicker = true
            }
    ) {
        SmallHeadingText(
            text = "Select Date",
            modifier = Modifier.padding(start = 16.dp, top = 10.dp)
        )
        DescriptionText(
            text = getDateTimeFromTimestamp(timeStamp),
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    if (showDatePicker) {
        DatePickerModal(onDateSelected = { dateTime ->
            dateTime?.let {
                callBack(it)
            }
            showDatePicker = false
        }) {
            showDatePicker = false
        }
    }
}

