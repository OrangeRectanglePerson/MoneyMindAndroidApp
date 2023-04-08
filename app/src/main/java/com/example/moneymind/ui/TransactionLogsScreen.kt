package com.example.moneymind.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moneymind.R
import com.example.moneymind.ViewModels.TransactionCategoryViewModel
import com.example.moneymind.ViewModels.TransactionLogViewModel
import com.example.moneymind.data.TransactionCategory
import com.example.moneymind.data.TransactionLog
import com.example.moneymind.ui.CommonUI.CategorySelectionDialog
import com.example.moneymind.ui.CommonUI.LogTransactionDialog
import com.example.moneymind.ui.CommonUI.TransactionLogCard
import com.example.moneymind.ui.Extras.putSerializable
import com.example.moneymind.ui.theme.CSProjectTheme
import com.example.moneymind.ui.theme.DarkCyan
import com.example.moneymind.ui.theme.SpringGreen
import java.util.*

@Composable
fun TransactionLogsScreen(
    _topBar : @Composable () -> Unit,
    _gotoCategoriesScreen : () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
    transactionCategoryViewModel: TransactionCategoryViewModel,
){

    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    var showTransactionCreationDialog by remember{ mutableStateOf(false) }

    var showFiltrationDialog by remember{ mutableStateOf(false) }
    val whitelistedCategories = ArrayList<TransactionCategory>()

    for(c in transactionCategoryViewModel.getCategoryList()){
        whitelistedCategories.add(c)
    }


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
                        item {
                            TextButton(
                                onClick = _gotoCategoriesScreen,
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
                                Text("Manage Categories", style = MaterialTheme.typography.button, textAlign = TextAlign.Center)
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(modifier = Modifier
                                .fillParentMaxWidth()
                                .wrapContentSize(Alignment.Center)
                            ){
                                item {
                                    TextButton(
                                        onClick = { showFiltrationDialog = true },
                                        modifier = Modifier
                                            .width(((context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density - 3 * 10) * (3.0 / 4.0)).dp)
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
                                            "Filter(s)",
                                            style = MaterialTheme.typography.button,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                                item {
                                    IconButton(
                                        onClick = {  },
                                        modifier = Modifier
                                            .width(((context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density - 3 * 10) * (1.0 / 4.0)).dp)
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
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.sorting_sign),
                                            contentDescription = "Sorting Button",
                                            modifier = Modifier.height(55.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(items = transactionLogsState.value.transactions){ transaction ->
                            val whitelistedCategoryNames = ArrayList<String>()
                            for( c in whitelistedCategories){
                                whitelistedCategoryNames.add(c.name)
                            }
                            var toBeConsidered = true
                            for(c in transaction.categories){
                                if (!whitelistedCategoryNames.contains(c.name)) toBeConsidered = false
                            }
                            if(toBeConsidered) {
                                TransactionLogCard(
                                    transaction = transaction,
                                    transactionCategoriesState = transactionCategoryViewModel.uiState.collectAsState().value,
                                    transactionLogsState = transactionLogsState.collectAsState().value
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }


                //logging dialog
                if(showTransactionCreationDialog) {
                    LogTransactionDialog(
                        transactionCategoriesState = transactionCategoryViewModel.uiState.collectAsState().value,
                        transactionLogsState = transactionLogsState.collectAsState().value,
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

                            //save changes
                            context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                                .putSerializable(
                                    "transactionsJSON",
                                    transactionLogsState.value
                                ).apply()

                            //toast to fulfill project requirements
                            Toast.makeText(context,"New Transaction Created!", Toast.LENGTH_SHORT).show()

                            showTransactionCreationDialog = false
                        }
                    )
                }

                if(showFiltrationDialog){
                    CategorySelectionDialog(
                        originalSelection = whitelistedCategories,
                        transactionCategoriesToChooseFrom = transactionCategoryViewModel.uiState.collectAsState().value,
                        onDismiss = { showFiltrationDialog = false },
                        onPositiveClick = {
                            whitelistedCategories.clear()
                            for (c in it) whitelistedCategories.add(c)

                            showFiltrationDialog = false
                        }
                    )
                }
            }
        }
    )
}





