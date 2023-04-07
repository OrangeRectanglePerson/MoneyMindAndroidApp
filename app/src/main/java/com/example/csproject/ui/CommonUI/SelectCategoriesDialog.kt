package com.example.csproject.ui.CommonUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.R
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.data.TransactionCategoriesState
import com.example.csproject.data.TransactionCategory
import com.example.csproject.ui.theme.CSProjectTheme
import com.example.csproject.ui.theme.DarkCyan
import com.example.csproject.ui.theme.LibreOfficeBlue
import com.example.csproject.ui.theme.LimeGreen

@Composable
fun CategorySelectionDialog(
    originalSelection : ArrayList<TransactionCategory>,
    transactionCategoriesToChooseFrom: TransactionCategoriesState,
    onDismiss: (selectedCategories : ArrayList<TransactionCategory>) -> Unit,
    onPositiveClick: (selectedCategories : ArrayList<TransactionCategory>) -> Unit
){

    var selectedCategories = ArrayList<TransactionCategory>()

    for(each in originalSelection) selectedCategories.add(each)

    CSProjectTheme() {
        Dialog(
            onDismissRequest = {selectedCategories},

        ) {
            Surface(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                elevation = 8.dp,
                color = DarkCyan
            ) {
                LazyColumn(
                        Modifier
                            .background(Color.Transparent)
                            .padding(10.dp, 10.dp, 10.dp, 10.dp),
                ) {

                    item{
                        val focusManager = LocalFocusManager.current

                        Text(
                            text = "Select Categories",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.h3,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        //Spacer(modifier = Modifier.height(5.dp))
                    }

                    items(items = transactionCategoriesToChooseFrom.categories){ category ->
                        var selected by remember { mutableStateOf(selectedCategories.contains(category)) }

                        Row(
                            Modifier.clickable {
                                if (!selected && !selectedCategories.contains(category)){
                                    selectedCategories.add(category)
                                    selected = true
                                } else {
                                    selectedCategories.remove(category)
                                    selected = false
                                }
                            }
                        ){

                            if(selected) {
                                Icon(
                                    painter = painterResource(id = R.drawable.selected),
                                    contentDescription = "selected icon",
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .align(Alignment.CenterVertically)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.unselected),
                                    contentDescription = "unselected icon",
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .align(Alignment.CenterVertically)
                                )
                            }

                            TextButton(
                                onClick = {
                                    if (!selected && !selectedCategories.contains(category)){
                                        selectedCategories.add(category)
                                        selected = true
                                    } else {
                                        selectedCategories.remove(category)
                                        selected = false
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colors.primary,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colors.primaryVariant,
                                        shape = RoundedCornerShape(30)
                                    ),
                            ){
                                Text(
                                    category.name,
                                    style = MaterialTheme.typography.button,
                                    textAlign = TextAlign.Center,
                                    color = category.color
                                )
                            }
                        }
                    }

                    //.......................................................................
                    item {
                        TextButton(
                            onClick = { onDismiss(selectedCategories) },
                            modifier = Modifier
                                .fillMaxWidth()
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
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onPositiveClick(selectedCategories)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = LimeGreen, shape = RoundedCornerShape(30))
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Confirm Selection",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun previewCategorySelectionDialog(){
    val TCVM : TransactionCategoryViewModel = viewModel()
    val TCS by remember { mutableStateOf(TCVM.uiState) }
    CategorySelectionDialog(
        originalSelection = ArrayList(),
        transactionCategoriesToChooseFrom = TCS.collectAsState().value,
        onDismiss = {_ -> },
        onPositiveClick = {_ -> }
    )
}