package com.example.csproject.ui


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.R
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionCategoriesState
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.CommonUI.AddCategoryDialog
import com.example.csproject.ui.CommonUI.EditCategoryDialog
import com.example.csproject.ui.CommonUI.LogTransactionDialog
import com.example.csproject.ui.Extras.putSerializable
import com.example.csproject.ui.theme.*
import java.util.*

@Composable
fun CategoriesScreen(
    _topBar : @Composable () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
    transactionCategoryViewModel: TransactionCategoryViewModel
){

    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    val transactionCategoriesState by remember{ mutableStateOf(transactionCategoryViewModel.uiState) }
    var showTransactionCreationDialog by remember{ mutableStateOf(false) }
    var showCategoryCreationDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = _topBar ,
        //floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(!showTransactionCreationDialog) showTransactionCreationDialog = true
                },
                backgroundColor = SpringGreen
            ) {
                Icon(
                    painter = painterResource(R.drawable.money_sign),
                    contentDescription = "",
                    tint = DarkCyan,
                )
            }
        },
        content = {
            CSProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopCenter),
                        contentPadding = PaddingValues(10.dp),
                    ) {
                        item {
                            TextButton(
                                onClick = { showCategoryCreationDialog = true },
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
                            ) {
                                Text(
                                    "Add Category",
                                    style = MaterialTheme.typography.button,
                                    textAlign = TextAlign.Center
                                )
                            }

                            //TextButton(
                            //    onClick = {
                            //        //val items = Json.decodeFromString(
                            //        //    TransactionCategoriesStateSerializer,
                            //        //    context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE)
                            //        //        .getString("categoriesJSON","")!!
                            //        //)


                            //    },
                            //    modifier = Modifier
                            //        .fillMaxWidth()
                            //        .background(
                            //            color = MaterialTheme.colors.primary,
                            //            shape = RoundedCornerShape(30)
                            //        )
                            //        .border(
                            //            width = 3.dp,
                            //            color = MaterialTheme.colors.primaryVariant,
                            //            shape = RoundedCornerShape(30)
                            //        ),
                            //) {
                            //    Text(
                            //        "SHAREDPREFERENCES",
                            //        style = MaterialTheme.typography.button,
                            //        textAlign = TextAlign.Center
                            //    )
                            //}

                            Spacer(modifier = Modifier.height(10.dp))
                        }


                        items(items = transactionCategoriesState.value.categories){ category ->
                            TransactionCategoryCard(
                                category = category,
                                transactionsLogsViewModel = transactionsLogViewModel,
                                transactionsCategoriesState = transactionCategoriesState.collectAsState().value
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }


                    }
                }

                //logging dialog
                if(showTransactionCreationDialog) {
                    LogTransactionDialog(
                        transactionCategoriesState = transactionCategoryViewModel.uiState.collectAsState().value,
                        onDismiss = { tName, tAmt, selectedCategories ->
                            showTransactionCreationDialog = false
                        },
                        onPositiveClick = { tName, tAmt, selectedCategories ->
                            val newTransactionLog = TransactionLog(
                                name = tName,
                                amount = tAmt,
                                date = Calendar.getInstance()
                            )
                            newTransactionLog.categories.clear()
                            for (each in selectedCategories) newTransactionLog.categories.add(each)
                            transactionsLogViewModel.addTransaction(
                                newTransactionLog
                            )
                            showTransactionCreationDialog = false
                        }
                    )
                }

                //create Category dialog
                if(showCategoryCreationDialog){
                    AddCategoryDialog(
                        transactionCategoriesState = transactionCategoriesState.collectAsState().value,
                        onDismiss = {name, color ->
                            showCategoryCreationDialog = false
                        },
                        onPositiveClick = {name, color ->
                            transactionCategoriesState.value.categories.add(
                                TransactionCategory(name, color)
                            )
                            //


                            //context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                            //    .putString(
                            //        "categoriesJSON",
                            //        Json.encodeToString(TransactionCategoriesStateSerializer,transactionCategoriesState.value)
                            //    ).apply()

                            context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                                .putSerializable(
                                    "categoriesJSON",
                                    transactionCategoriesState.value
                                ).apply()

                            //Log.d("json1", context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).getString(s))
                            //Log.d("json2", Json.encodeToString(TransactionCategoriesStateSerializer,transactionCategoriesState.value))

                            showCategoryCreationDialog = false
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun TransactionCategoryCard(
    category: TransactionCategory,
    transactionsLogsViewModel: TransactionLogViewModel,
    transactionsCategoriesState: TransactionCategoriesState,
){
    val context = LocalContext.current
    var numTransactionWithCategory = 0
    for(r in transactionsLogsViewModel.uiState.collectAsState().value.transactions){
        if(r.categories.contains(category)) numTransactionWithCategory++
    }

    var showCategoryEditDialog by remember { mutableStateOf(false) }
    var showDeleteCategoryAlertDialog by remember { mutableStateOf(false) }

    CSProjectTheme {
        Card (
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.clickable {
                //Toast.makeText(context, String.format("Card for category \"%s\"", category.name), Toast.LENGTH_SHORT).show()
                showCategoryEditDialog = true
            },
        ){
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(20)
                    )
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(10.dp),
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                    color = category.color
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = String.format("%d Items", numTransactionWithCategory),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

        }

        if(showCategoryEditDialog){
            EditCategoryDialog(
                categoryToEdit = category,
                transactionCategoriesState = transactionsCategoriesState,
                onDismiss = {_,_ ->
                    showCategoryEditDialog = false
                },
                onPositiveClick = {newCategoryName, newCategoryColor ->
                    category.name = newCategoryName
                    category.color = newCategoryColor

                    //save changes
                    context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                        .putSerializable(
                            "categoriesJSON",
                            transactionsCategoriesState
                        ).apply()

                    showCategoryEditDialog = false
                },
                onNegativeClick = {_,_ ->
                    showDeleteCategoryAlertDialog = true
                }
            )
        }

        if(showDeleteCategoryAlertDialog){
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                shape = RoundedCornerShape(5.dp),
                backgroundColor = DarkCyan.copy(alpha = 0.9f),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back button.
                    // If you want to disable that functionality, simply leave this block empty.
                    showDeleteCategoryAlertDialog = false
                },
                title = {
                    Text(
                        text = "Confirm Deletion",
                        style = MaterialTheme.typography.h3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete \"${category.name}\"?",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // perform the confirm action and
                            // close the dialog

                            //first remove category from all transactions
                            for (t in transactionsLogsViewModel.uiState.value.transactions){
                                transactionsLogsViewModel.removeCategoryFromTransaction(t, category)
                            }


                            //then remove category from array of categories
                            transactionsCategoriesState.categories.remove(category)

                            //save changes
                            context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                                .putSerializable(
                                    "categoriesJSON",
                                    transactionsCategoriesState
                                ).apply()

                            //close both dialogs
                            showCategoryEditDialog = false
                            showDeleteCategoryAlertDialog = false
                        },
                        modifier = Modifier
                            .background(color = FireBrick, shape = RoundedCornerShape(30))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colors.primaryVariant,
                                shape = RoundedCornerShape(30)
                            )
                            .padding(5.dp),
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            // close the dialog
                            showDeleteCategoryAlertDialog = false
                        },
                        modifier = Modifier
                            .background(color = LibreOfficeBlue, shape = RoundedCornerShape(30))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colors.primaryVariant,
                                shape = RoundedCornerShape(30)
                            )
                            .padding(5.dp),
                    ) {
                        Text(text = "Dismiss")
                    }
                },
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun previewTCC(){
    val TLVM : TransactionLogViewModel = viewModel()
    val transactionLogsState by remember{ mutableStateOf(TLVM.uiState) }
    val TCVM : TransactionCategoryViewModel = viewModel()
    val transactionCategoriesState by remember{ mutableStateOf(TCVM.uiState) }

    val dummyCategory = TransactionCategory("Category", FireBrick)

    TCVM.addCategory(dummyCategory)

    val dummyTransaction = TransactionLog("Dummy", 0.0, Calendar.getInstance())

    TLVM.addTransaction(dummyTransaction)

    TLVM.addCategoryToTransaction(dummyTransaction, dummyCategory)

    TransactionCategoryCard(category = dummyCategory, transactionsLogsViewModel = TLVM, transactionCategoriesState.collectAsState().value)


}