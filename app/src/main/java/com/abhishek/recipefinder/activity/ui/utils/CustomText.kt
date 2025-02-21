package com.abhishek.recipefinder.activity.ui.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun CustomText(
    text: String, // Mandatory
    color: Color, // Mandatory
    fontSize: TextUnit, // Mandatory
    fontWeight: FontWeight, // Mandatory
    textAlign: TextAlign, // Mandatory
    modifier: Modifier = Modifier, // Optional (default value allowed)
    style: TextStyle
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        modifier = modifier,
        style =style
    )
}