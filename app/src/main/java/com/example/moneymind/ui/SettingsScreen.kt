package com.example.moneymind.ui

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneymind.ViewModels.TransactionLogViewModel
import com.example.moneymind.ui.Extras.putSerializable
import com.example.moneymind.ui.theme.CSProjectTheme

@Composable
fun SettingsScreen(
    _topBar : @Composable () -> Unit,
    gotoPreviousScreenAction : () -> Unit,
    helpOption : String,
    transactionLogViewModel: TransactionLogViewModel,
){

    val context = LocalContext.current

    val transactionLogsState by remember { mutableStateOf(transactionLogViewModel.uiState) }
    var moneyUnit by remember{ mutableStateOf(transactionLogsState.value.moneyUnit) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = _topBar,
        content = {
            CSProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.secondary
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopCenter)
                            .verticalScroll(state = rememberScrollState())
                            .padding(30.dp)
                    ){

                        TextButton(
                            onClick = gotoPreviousScreenAction,
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
                            Text("< Go Back", style = MaterialTheme.typography.button, textAlign = TextAlign.Center)
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        OutlinedTextField(
                            value = moneyUnit,
                            onValueChange = {
                                moneyUnit = it
                                transactionLogsState.value.moneyUnit = it
                                //save changes
                                context.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE).edit()
                                    .putSerializable(
                                        "transactionsJSON",
                                        transactionLogsState.value
                                    ).apply()
                            },
                            label = { Text("Preferred Money Unit", style = MaterialTheme.typography.subtitle1.copy(fontSize = 36.sp)) },
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                            textStyle = MaterialTheme.typography.body1.copy(fontSize = 36.sp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = "Greetings from The Developer!\n"+
                                    "This app was created by Devon Lim in 2023 as part of NUS High School of Math and Science's Year 4 CS Curriculum.\n" +
                                    "Hope you find this app to be useful!\n\n" +
                                    "The FontStruction \"Pokémon DP Pro\" (https://fontstruct.com/fontstructions/show/404271) by “crystalwalrein” is licensed under a Creative Commons Attribution Share Alike license (http://creativecommons.org/licenses/by-sa/3.0/).",
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }
            }
        },
        bottomBar = {
            CSProjectTheme {
//                  createSnackbar(
//                      text = "Going to Operating Instructions Activity",
//                      showDialog = openSnackbar
//                  )
            }
        }
    )
}