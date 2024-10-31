package com.app.todo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HeadingText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 17.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
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
        color = textColor
    )
}

@Composable
fun DescriptionText(text: String, modifier: Modifier = Modifier, textColor: Color = Color.Black) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier,
        color = textColor
    )
}