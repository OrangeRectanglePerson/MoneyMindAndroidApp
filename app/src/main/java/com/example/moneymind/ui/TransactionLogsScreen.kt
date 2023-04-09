package com.example.moneymind.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.moneymind.R
import com.example.moneymind.ViewModels.TransactionCategoryViewModel
import com.example.moneymind.ViewModels.TransactionLogViewModel
import com.example.moneymind.data.TransactionCategory
import com.example.moneymind.data.TransactionLog
import com.example.moneymind.ui.CommonUI.CategorySelectionDialog
import com.example.moneymind.ui.CommonUI.LogTransactionDialog
import com.example.moneymind.ui.CommonUI.TransactionLogCard
import com.example.moneymind.ui.Extras.TransactionLogComparators
import com.example.moneymind.ui.Extras.putSerializable
import com.example.moneymind.ui.theme.*
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

    var showSortingDialog by remember{ mutableStateOf(false) }


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
                                        onClick = { showSortingDialog = true },
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

                if(showSortingDialog){
                    ChooseSortingDialog(
                        onDismiss = { showSortingDialog = false },
                        onPositiveClick = {

                            transactionsLogViewModel.sortTransaction(it)

                            //save changes
                            context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                                .putSerializable(
                                    "transactionsJSON",
                                    transactionLogsState.value
                                ).apply()

                            showSortingDialog = false

                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ChooseSortingDialog(
    onDismiss: (selectedComparator : Comparator<TransactionLog>) -> Unit,
    onPositiveClick: (selectedComparator : Comparator<TransactionLog>) -> Unit
){

    var nulled by remember { mutableStateOf(true) }
    var compareBy by remember { mutableStateOf(TransactionLogComparators.COMPARE_BY_NAME) }
    var reverseOrder by remember { mutableStateOf(false) }

    CSProjectTheme() {
        Dialog(
            onDismissRequest = { onDismiss(TransactionLogComparators.emptyComparator) },

            ) {
            Surface(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                elevation = 8.dp,
                color = DarkCyan
            ) {
                Column(
                    Modifier
                        .background(Color.Transparent)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                        .verticalScroll(enabled = true, state = rememberScrollState()),
                ) {
                    Text(
                        text = "Sort Transactions",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h3,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(10.dp))

                    Column(
                        Modifier
                            .padding(start = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        TextButton(
                            shape = RoundedCornerShape(30),
                            onClick = {
                                compareBy = TransactionLogComparators.COMPARE_BY_NAME
                                nulled = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    color = LibreOfficeBlue,
                                    shape = RoundedCornerShape(30)
                                )
                                .border(
                                    width = 5.dp,
                                    color = if (!nulled && compareBy == TransactionLogComparators.COMPARE_BY_NAME) FireBrick else MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Sort By Name",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                        TextButton(
                            shape = RoundedCornerShape(30),
                            onClick = {
                                compareBy = TransactionLogComparators.COMPARE_BY_VALUE
                                nulled = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    color = LibreOfficeBlue,
                                    shape = RoundedCornerShape(30)
                                )
                                .border(
                                    width = 5.dp,
                                    color = if (!nulled && compareBy == TransactionLogComparators.COMPARE_BY_VALUE) FireBrick else MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Sort By Value",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                            )
                        }
                        TextButton(
                            shape = RoundedCornerShape(30),
                            onClick = {
                                compareBy = TransactionLogComparators.COMPARE_BY_DATE
                                nulled = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    color = LibreOfficeBlue,
                                    shape = RoundedCornerShape(30)
                                )
                                .border(
                                    width = 5.dp,
                                    color = if (!nulled && compareBy == TransactionLogComparators.COMPARE_BY_DATE) FireBrick else MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(5.dp),
                        ) {
                            Text(
                                "Sort By Date",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(
                            Modifier
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .align(Alignment.CenterHorizontally)
                        ){
                            TextButton(
                                shape = RoundedCornerShape(30),
                                onClick = {
                                    reverseOrder = true
                                    nulled = false
                                },
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .align(Alignment.CenterVertically)
                                    .background(
                                        color = LibreOfficeBlue,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = if (!nulled && reverseOrder) FireBrick else MaterialTheme.colors.primaryVariant,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(5.dp),
                            ) {
                                Text(
                                    "dec",
                                    style = MaterialTheme.typography.button,
                                    color = Color.Black,
                                )
                            }

                            Spacer(Modifier.width(10.dp))

                            TextButton(
                                shape = RoundedCornerShape(30),
                                onClick = {
                                    reverseOrder = false
                                    nulled = false
                                },
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .align(Alignment.CenterVertically)
                                    .background(
                                        color = LibreOfficeBlue,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = if (!nulled && !reverseOrder) FireBrick else MaterialTheme.colors.primaryVariant,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(5.dp),
                            ) {
                                Text(
                                    "inc",
                                    style = MaterialTheme.typography.button,
                                    color = Color.Black,
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            onDismiss(TransactionLogComparators.emptyComparator)
                         },
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
                            if(nulled) onPositiveClick(TransactionLogComparators.emptyComparator)
                            else {
                                if (reverseOrder) {
                                    when (compareBy) {
                                        TransactionLogComparators.COMPARE_BY_DATE -> onPositiveClick(
                                            TransactionLogComparators.compareByDateReverseOrder
                                        )
                                        TransactionLogComparators.COMPARE_BY_VALUE -> onPositiveClick(
                                            TransactionLogComparators.compareByValueReverseOrder
                                        )
                                        TransactionLogComparators.COMPARE_BY_NAME -> onPositiveClick(
                                            TransactionLogComparators.compareByNameReverseOrder
                                        )
                                        else -> onPositiveClick(TransactionLogComparators.emptyComparator)
                                    }
                                } else {
                                    when (compareBy) {
                                        TransactionLogComparators.COMPARE_BY_DATE -> onPositiveClick(
                                            TransactionLogComparators.compareByDateProperOrder
                                        )
                                        TransactionLogComparators.COMPARE_BY_VALUE -> onPositiveClick(
                                            TransactionLogComparators.compareByValueProperOrder
                                        )
                                        TransactionLogComparators.COMPARE_BY_NAME -> onPositiveClick(
                                            TransactionLogComparators.compareByNameProperOrder
                                        )
                                        else -> onPositiveClick(TransactionLogComparators.emptyComparator)
                                    }
                                }
                            }
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
                            "Sort Transactions",
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

@Preview(showBackground = true)
@Composable
fun previewSortingDialog(){
    ChooseSortingDialog(onDismiss = {}, onPositiveClick = {})
}





