package com.example.csproject.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = pokemonDPPro_fonts,
    h1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 120.sp,
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 50.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        color = Color.Black,
    ),
    button = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        color = Color.Black,
        letterSpacing = 1.5.sp,

    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),


)