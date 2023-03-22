package com.example.csproject.ui.CommonUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionCategoriesState
import com.example.csproject.data.TransactionLog
import com.example.csproject.data.TransactionLogsState
import com.example.csproject.ui.theme.CSProjectTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionLogCard(
    transactionLogsState: TransactionLogsState,
    transaction : TransactionLog,
    transactionCategoriesState: TransactionCategoriesState
){

    var showTransactionEditDialog by remember{ mutableStateOf(false) }

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
                        showTransactionEditDialog = false
                    },
                    onNegativeClick = { tName, tAmt, selectedCategories ->

                        transactionLogsState.transactions.remove(transaction)

                        showTransactionEditDialog = false
                    },
                )
            }

        }

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