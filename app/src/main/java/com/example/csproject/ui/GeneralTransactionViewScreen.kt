package com.example.csproject.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.csproject.R
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.CommonUI.LogTransactionDialog
import com.example.csproject.ui.CommonUI.TransactionLogCard
import com.example.csproject.ui.theme.CSProjectTheme
import com.example.csproject.ui.theme.DarkCyan
import com.example.csproject.ui.theme.SpringGreen
import java.util.*

@Composable
fun GeneralTransactionViewScreen(
    _topBar : @Composable () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
    transactionCategoryViewModel: TransactionCategoryViewModel,
){

    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    var showTransactionCreationDialog by remember{ mutableStateOf(false) }


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
        }
        , content = {
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

                        items(items = transactionLogsState.value.transactions){ transaction ->
                            TransactionLogCard(
                                transaction = transaction ,
                                transactionCategoriesState = transactionCategoryViewModel.uiState.collectAsState().value,
                                transactionLogsState = transactionLogsState.collectAsState().value
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
            }
        }
    )
}




