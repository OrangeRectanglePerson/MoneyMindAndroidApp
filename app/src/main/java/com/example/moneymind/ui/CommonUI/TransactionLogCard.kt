package com.example.moneymind.ui.CommonUI

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.moneymind.data.TransactionCategoriesState
import com.example.moneymind.data.TransactionLog
import com.example.moneymind.data.TransactionLogsState
import com.example.moneymind.ui.Extras.putSerializable
import com.example.moneymind.ui.theme.CSProjectTheme
import com.example.moneymind.ui.theme.DarkCyan
import com.example.moneymind.ui.theme.FireBrick
import com.example.moneymind.ui.theme.LibreOfficeBlue
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionLogCard(
    transactionLogsState: TransactionLogsState,
    transaction : TransactionLog,
    transactionCategoriesState: TransactionCategoriesState
){

    var showTransactionEditDialog by remember{ mutableStateOf(false) }
    var showDeleteTransactionAlertDialog by remember{ mutableStateOf(false) }

    val context = LocalContext.current

    CSProjectTheme() {
        Card (
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.clickable {
                showTransactionEditDialog = true
            }
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
                Text(transaction.name, style = MaterialTheme.typography.body1, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(5.dp))

                val SDF = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())
                Text(SDF.format(transaction.date.time), style = MaterialTheme.typography.body1, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(5.dp))

                Text(String.format("$ %.2f", transaction.amount), style = MaterialTheme.typography.body1, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = buildAnnotatedString {
                        for(category in transaction.categories){
                            withStyle(style = SpanStyle(color = category.color)) {
                                append(category.name)
                            }
                            append(", ")
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }


            if(showTransactionEditDialog) {
                EditTransactionDialog(
                    transactionToEdit = transaction,
                    transactionLogsState = transactionLogsState,
                    transactionCategoriesState = transactionCategoriesState,
                    onDismiss = { tName, tAmt, selectedCategories ->
                        showTransactionEditDialog = false
                    },
                    onPositiveClick = { tName, tAmt, selectedCategories ->
                        transaction.name = tName
                        transaction.amount = tAmt
                        transaction.categories.clear()
                        for (i in selectedCategories) {
                            transaction.categories.add(i)
                        }

                        //save changes
                        context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                            .putSerializable(
                                "transactionsJSON",
                                transactionLogsState
                            ).apply()

                        showTransactionEditDialog = false
                    },
                    onNegativeClick = { tName, tAmt, selectedCategories ->

                        //ask for confirmation before deletting in the alertdialog
                        showDeleteTransactionAlertDialog = true

                        //if delete goes through, alert dialog will also close edit dialog
                    },
                )
            }

            if(showDeleteTransactionAlertDialog) {
                ConfirmDeleteTransactionAlertDialog(
                    onDismiss = {
                        //go back to editing dialog, close only the alert dialog
                        showDeleteTransactionAlertDialog = false
                    },
                    onConfirm = {
                        //actually delete the transaction
                        transactionLogsState.transactions.remove(transaction)

                        //save changes
                        context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                            .putSerializable(
                                "transactionsJSON",
                                transactionLogsState
                            ).apply()

                        //close both edit and alert dialogs after deleting the transaction
                        showTransactionEditDialog = false
                        showDeleteTransactionAlertDialog = false
                    },
                    transactionToDeleteName = transaction.name
                )
            }

        }

    }


}

@Composable
fun ConfirmDeleteTransactionAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    transactionToDeleteName : String
){
    CSProjectTheme() {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = DarkCyan.copy(alpha = 0.9f),
            onDismissRequest = onDismiss,
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
                    text = "Are you sure you want to delete \"${transactionToDeleteName}\"?",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
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
                    onClick = onDismiss,
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

/*
@Preview(showBackground = false)
@Composable
fun previewTLC(){

    val dummyTransactionLog0 = TransactionLog("Dummy Transaction", 1.00, Calendar.getInstance())
    dummyTransactionLog0.categories.add(TransactionCategoryViewModel.dummyCategory0)
    dummyTransactionLog0.categories.add(TransactionCategoryViewModel.dummyCategory0)

    TransactionLogCard(transaction = dummyTransactionLog0 )
}

 */