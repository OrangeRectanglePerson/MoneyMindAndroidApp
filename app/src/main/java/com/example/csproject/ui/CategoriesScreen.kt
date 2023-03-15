package com.example.csproject.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.R
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.data.TransactionLogsState
import com.example.csproject.ui.CommonUI.LogTransactionDialog
import com.example.csproject.ui.CommonUI.TransactionLogCard
import com.example.csproject.ui.theme.CSProjectTheme
import com.example.csproject.ui.theme.DarkCyan
import com.example.csproject.ui.theme.FireBrick
import com.example.csproject.ui.theme.SpringGreen
import java.text.SimpleDateFormat
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
                                onClick = { },
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

                            Spacer(modifier = Modifier.height(10.dp))
                        }


                        items(items = transactionCategoriesState.value.categories){ category ->
                            TransactionCategoryCard(category = category, transactionsLogsState = transactionLogsState.collectAsState().value)
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


@Composable
fun TransactionCategoryCard(
    category: TransactionCategory,
    transactionsLogsState: TransactionLogsState
){
    val context = LocalContext.current
    var numTransactionWithCategory = 0
    for(r in transactionsLogsState.transactions){
        if(r.categories.contains(category)) numTransactionWithCategory++
    }

    CSProjectTheme {
        Card (
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.clickable {
                Toast.makeText(context, String.format("Card for category \"%s\"", category.name), Toast.LENGTH_SHORT).show()
            },
        ){
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(20)
                    )
                    //.border(
                    //    width = 3.dp,
                    //    color = MaterialTheme.colors.secondaryVariant,
                    //    shape = RoundedCornerShape(30)
                    //)
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

    }


}

@Preview(showBackground = false)
@Composable
fun previewTCC(){
    val TLVM : TransactionLogViewModel = viewModel()
    val transactionLogsState by remember{ mutableStateOf(TLVM.uiState) }
    val TCVM : TransactionCategoryViewModel = viewModel()
    val transactionCategoriesState by remember{ mutableStateOf(TLVM.uiState) }

    val dummyCategory = TransactionCategory("Category", FireBrick)

    TCVM.addCategory(dummyCategory)

    val dummyTransaction = TransactionLog("Dummy", 0.0, Calendar.getInstance())

    TLVM.addTransaction(dummyTransaction)

    TLVM.addCategoryToTransaction(dummyTransaction, dummyCategory)

    TransactionCategoryCard(category = dummyCategory, transactionsLogsState = transactionLogsState.collectAsState().value)


}