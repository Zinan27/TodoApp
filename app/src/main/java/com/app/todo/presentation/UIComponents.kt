package com.app.todo.presentation

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.todo.R
import com.app.todo.presentation.screens.ui.theme.LightBlue
import com.app.todo.presentation.screens.ui.theme.PrimaryColor
import com.app.todo.utils.getDateTimeFromTimestamp

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
fun CustomTextField(modifier: Modifier, hint: String = "") {
    var text by remember { mutableStateOf("") }
    TextField(

        maxLines = 2,
        value = text,
        onValueChange = {
            text = it
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
fun TodoItem(callBack: () -> Unit = {}) {

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

        Box(modifier = Modifier
            .padding(10.dp)
            .size(15.dp)
            .background(Color.Red, shape = CircleShape)
            .constrainAs(indicator) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            })

        SmallHeadingText(text = "This is title", modifier = Modifier.constrainAs(headingText) {
            top.linkTo(icon.top)
            start.linkTo(icon.end, margin = 5.dp)
        })

        DescriptionText(text = "10 Oct 2024", modifier = Modifier.constrainAs(descriptionText) {
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
fun ChooseIconField(modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconImage(icon = R.drawable.work)
        IconImage(icon = R.drawable.medicine)
        IconImage(icon = R.drawable.education)
        IconImage(icon = R.drawable.exercise)
        IconImage(icon = R.drawable.food)
        IconImage(icon = R.drawable.prayer)
    }
}

@Composable
fun IconImage(icon: Int) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = "Work Icon",
        modifier = Modifier.size(30.dp)
    )
}


@Composable
fun DatePickerField(modifier: Modifier) {

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
            text = "15 Oct, 2023",
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    if (showDatePicker) {
        DatePickerModal(onDateSelected = { dateTime ->
            dateTime?.let {
                val date = getDateTimeFromTimestamp(it)
                Log.d("getDateTimeFromTimestamp", date)
            }
            showDatePicker = false
        }) {
            showDatePicker = false
        }
    }
}

