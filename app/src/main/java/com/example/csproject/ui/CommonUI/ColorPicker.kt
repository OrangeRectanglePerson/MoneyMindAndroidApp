package com.example.csproject.ui.CommonUI

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.csproject.ui.theme.CategoryColorsArray
import com.example.csproject.ui.theme.LibreOfficeBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPicker(listOfColors : Array<Color>, whatToDoWhenColorSelected : (selectedColor : Color) -> Unit){


    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.height(300.dp)
    ) {

        items(items = listOfColors) { currColor ->

            Button(
                onClick = { whatToDoWhenColorSelected(currColor) },
                modifier = Modifier
                    .padding(0.dp)
                    .defaultMinSize(Dp.Infinity, 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = currColor),
                border = BorderStroke(width = 2.dp, color = LibreOfficeBlue)
            ) {}

        }

    }


}

@Preview(showBackground = false)
@Composable
fun previewColorPicker(){
    ColorPicker(listOfColors = CategoryColorsArray) { _ -> }
}