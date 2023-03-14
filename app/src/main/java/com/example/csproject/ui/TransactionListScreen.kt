package com.example.csproject.ui

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.csproject.R
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.CommonUI.makeButton
import com.example.csproject.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*



@Composable
fun TransactionListScreen(
    _topBar : @Composable () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
){

    var timesFabPressed by remember{ mutableStateOf(0) }
    var timesButtonPressed by remember{ mutableStateOf(0) }
    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    var showTransactionCreationDialog by remember{ mutableStateOf(false) }


    Scaffold(
        topBar = _topBar ,
        //floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    timesFabPressed++
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
                        item{
                            Text("Hello Android!", fontSize = 30.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(String.format("Times FAB Pressed : %d", timesFabPressed), fontSize = 30.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row{
                                Column(modifier = Modifier
                                    .widthIn(0.dp, 200.dp)
                                ){
                                    Text(String.format("Times Button Pressed : %d", timesButtonPressed), fontSize = 30.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                makeButton(onclick = {
                                    timesButtonPressed++
                                    Toast.makeText(context, "Button Pressed!", Toast.LENGTH_SHORT).show()
                                }, text = String.format("This button has been pressed %d times", timesButtonPressed))
                            }

                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        items(items = transactionLogsState.value.transactions){ transaction ->
                            TransactionLogCard(transaction = transaction )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        //container for my dialog
                        item {
                            if(showTransactionCreationDialog) {
                                LogTransactionDialog(
                                    onDismiss = { tName, tAmt ->
                                        showTransactionCreationDialog = false
                                    },
                                    onPositiveClick = { tName, tAmt ->
                                        transactionsLogViewModel.addTransaction(
                                            TransactionLog(
                                                name = tName,
                                                amount = tAmt,
                                                date = Calendar.getInstance()
                                            )
                                        )
                                        showTransactionCreationDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TransactionLogCard(
    transaction : TransactionLog
){
    CSProjectTheme() {
        Card (
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
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
            }

        }

    }


}

@Preview(showBackground = false)
@Composable
fun previewTLC(){
    TransactionLogCard(transaction = TransactionLog(name = "Example Transaction", amount = 10.00, date = Calendar.getInstance()))
}

@Composable
fun LogTransactionDialog(
    onDismiss: (tName : String, tAmt : Double) -> Unit,
    onPositiveClick: (tName : String, tAmt : Double) -> Unit
){
    var transactionName by rememberSaveable { mutableStateOf("My Transaction") }
    var transactionAmountText by rememberSaveable { mutableStateOf("") }
    var transactionAmount by rememberSaveable { mutableStateOf(0.0) }
    var isValidDouble by rememberSaveable { mutableStateOf(false) }

    CSProjectTheme() {
        Dialog(
            onDismissRequest = {onDismiss(transactionName, transactionAmount)},
        ) {
            Surface(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier
                    .padding(10.dp, 5.dp, 10.dp, 10.dp),
                elevation = 8.dp,
                color = DarkCyan
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
                            text = "New Transaction Log",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.h3,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Log a new Transaction",
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
                                text = "This is not a valid amount of money",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.caption,
                                color = Color(122, 0, 25)
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
                            onClick = { onDismiss(transactionName, transactionAmount) },
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
                                if(isValidDouble) onPositiveClick(transactionName, transactionAmount)
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
                            enabled = isValidDouble,
                        ) {
                            Text(
                                "Log Transaction",
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
fun previewLoggingDialog(){
    LogTransactionDialog(
        onDismiss = {_,_ -> },
        onPositiveClick = {_,_ -> }
    )
}

