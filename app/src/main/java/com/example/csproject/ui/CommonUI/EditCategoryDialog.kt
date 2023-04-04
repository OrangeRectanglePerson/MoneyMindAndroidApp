package com.example.csproject.ui.CommonUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.csproject.data.TransactionCategory
import com.example.csproject.ui.theme.*

@Composable
fun EditCategoryDialog(
    categoryToEdit : TransactionCategory,
    onDismiss: (tName : String, tAmt : Color) -> Unit,
    onPositiveClick: (tName : String, tAmt : Color) -> Unit,
    onNegativeClick: (tName : String, tAmt : Color) -> Unit
){
    var categoryName by rememberSaveable { mutableStateOf(categoryToEdit.name) }
    var categoryColor by rememberSaveable { mutableStateOf(categoryToEdit.color.toArgb()) }

    var showNestedDialog by remember { mutableStateOf(false) }


    CSProjectTheme() {
        Dialog(
            onDismissRequest = {onDismiss(categoryName, Color(categoryColor))},
        ) {
            Surface(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                elevation = 8.dp,
                color = DarkCyan.copy(alpha = 0.9f)
            ) {
                Column(
                    Modifier
                        .background(Color.Transparent)
                        .verticalScroll(enabled = true, state = rememberScrollState())
                ) {

                    /*
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Fit,
                    colorFilter  = ColorFilter.tint(
                        color = Purple40
                    ),
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .height(70.dp)
                        .fillMaxWidth(),

                    )

                 */

                    Column(modifier = Modifier
                        .padding(16.dp)
                    ) {
                        val focusManager = LocalFocusManager.current

                        Text(
                            text = "Edit Category",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.h3,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Edit existing Category",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.body1
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = {
                                categoryName = it
                            },
                            label = {
                                Text(
                                    "Category Name",
                                    style = MaterialTheme.typography.subtitle1
                                )
                            },
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = Color(categoryColor)
                            )
                        )

                        Spacer(modifier = Modifier.height(5.dp))


                    }

                    ColorPicker(listOfColors = CategoryColorsArray, whatToDoWhenColorSelected = { newColor ->
                        categoryColor = newColor.toArgb()
                    })

                    Spacer(modifier = Modifier.height(5.dp))


                    //.......................................................................
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
                            .wrapContentSize(align = Alignment.Center),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onPositiveClick(categoryName, Color(categoryColor))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(color = LimeGreen, shape = RoundedCornerShape(30))
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Edit Category",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                        TextButton(
                            onClick = { onDismiss(categoryName, Color(categoryColor)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(color = LibreOfficeBlue, shape = RoundedCornerShape(30))
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Cancel",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                            )
                        }
                        TextButton(
                            onClick = { onNegativeClick(categoryName, Color(categoryColor)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(color = FireBrick, shape = RoundedCornerShape(30))
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Delete Category",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}