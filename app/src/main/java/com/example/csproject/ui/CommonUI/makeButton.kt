package com.example.csproject.ui.CommonUI

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun makeButton(onclick : () -> Unit, text : String){
    Button(
        onclick
    ) {
        Text(text, fontSize = 28.sp)
    }
}