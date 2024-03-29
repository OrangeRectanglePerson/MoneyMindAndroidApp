package com.example.moneymind.ui.CommonUI

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymind.ViewModels.TransactionCategoryViewModel
import com.example.moneymind.data.TransactionCategoriesState
import com.example.moneymind.data.TransactionCategory
import com.example.moneymind.data.TransactionLog
import com.example.moneymind.data.TransactionLogsState
import com.example.moneymind.ui.theme.*
import java.util.*

@Composable
fun EditTransactionDialog(
    transactionToEdit : TransactionLog,
    transactionCategoriesState: TransactionCategoriesState,
    transactionLogsState: TransactionLogsState,
    onDismiss: (tName : String, tAmt : Double, selectedCategories : ArrayList<TransactionCategory>) -> Unit,
    onPositiveClick: (tName : String, tAmt : Double, selectedCategories : ArrayList<TransactionCategory>) -> Unit,
    onNegativeClick: (tName : String, tAmt : Double, selectedCategories : ArrayList<TransactionCategory>) -> Unit,
){
    var transactionName by rememberSaveable { mutableStateOf(transactionToEdit.name) }
    var transactionAmount by rememberSaveable { mutableStateOf(transactionToEdit.amount) }
    var transactionAmountText by rememberSaveable { mutableStateOf(transactionAmount.toString()) }
    var isValidDouble by rememberSaveable { mutableStateOf(true) }

    var showNestedDialog by remember { mutableStateOf(false) }

    var selectedCategories : ArrayList<TransactionCategory> = ArrayList()
    for(i in transactionToEdit.categories){
        selectedCategories.add(i)
    }

    val listOfUsedTransactionNames = ArrayList<String>()
    for(t in transactionLogsState.transactions) {
        if(t.name != transactionToEdit.name) listOfUsedTransactionNames.add(t.name)
    }

    CSProjectTheme() {
        Dialog(
            onDismissRequest = {onDismiss(transactionName, transactionAmount, selectedCategories)},
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
                            text = "Edit Transaction Log",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.h3,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Edit a Transaction",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.body1
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = transactionName,
                            onValueChange = {
                                transactionName = it
                            },
                            label = { Text("Transaction Name", style = MaterialTheme.typography.subtitle1) },
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                            textStyle = MaterialTheme.typography.body1
                        )

                        if(listOfUsedTransactionNames.contains(transactionName)) {
                            Text(
                                text = "This transaction name already exists!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.caption,
                                color = Color(122, 0, 25)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = transactionAmountText,
                            onValueChange = {
                                try {
                                    transactionAmountText = it
                                    transactionAmount = it.toDouble()
                                    isValidDouble = true
                                } catch (_ : NumberFormatException ){
                                    isValidDouble = false
                                }
                                if(transactionAmount.isInfinite() || transactionAmount.isNaN()){
                                    isValidDouble = false
                                }
                            },
                            label = { Text(text = "Transaction Amount", style = MaterialTheme.typography.subtitle1) },
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                            textStyle = MaterialTheme.typography.body1
                        )

                        if(!isValidDouble) {
                            Text(
                                text = "This is not a valid amount of money!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.caption,
                                color = Color(122, 0, 25)
                            )
                        }



                        Spacer(modifier = Modifier.height(5.dp))
                        TextButton(
                            onClick = { showNestedDialog = true },
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
                                "Edit Categories",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                            )
                        }
                        if(showNestedDialog){
                            CategorySelectionDialog(
                                originalSelection = selectedCategories,
                                transactionCategoriesToChooseFrom = transactionCategoriesState,
                                onDismiss = {showNestedDialog = false},
                                onPositiveClick = { newlySelectedCategories ->
                                    selectedCategories = newlySelectedCategories
                                    showNestedDialog = false
                                }
                            )
                        }
                    }


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
                                if(isValidDouble) onPositiveClick(transactionName, transactionAmount, selectedCategories)
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
                            enabled = isValidDouble && !listOfUsedTransactionNames.contains(transactionName),
                        ) {
                            Text(
                                "Confirm",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                        TextButton(
                            onClick = { onDismiss(transactionName, transactionAmount, selectedCategories) },
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
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                if(isValidDouble) onNegativeClick(transactionName, transactionAmount, selectedCategories)
                            },
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
                            enabled = isValidDouble,
                        ) {
                            Text(
                                "Delete Transaction",
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
fun previewEditingDialog(){
    val TCVM : TransactionCategoryViewModel = viewModel()
    val TCS by remember { mutableStateOf(TCVM.uiState) }
    EditTransactionDialog(
        transactionToEdit = TransactionLog("name", 20.23, Calendar.getInstance()),
        transactionLogsState = TransactionLogsState(),
        transactionCategoriesState = TCS.collectAsState().value,
        onDismiss = {_,_,_ -> },
        onPositiveClick = {_,_,_ -> },
        onNegativeClick = {_,_,_ -> },
    )
}