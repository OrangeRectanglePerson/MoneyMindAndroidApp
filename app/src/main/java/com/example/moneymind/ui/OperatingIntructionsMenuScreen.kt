package com.example.moneymind.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moneymind.R
import com.example.moneymind.ui.theme.CSProjectTheme
import com.example.moneymind.ui.theme.MediumTurquoise

@Composable
fun OperatingInstructionsMenuScreen(
    _topBar : @Composable () -> Unit,
    gotoLoggingHelpButtonAction : () -> Unit,
    gotoCategorisationHelpButtonAction : () -> Unit,
    gotoGraphingHelpButtonAction : () -> Unit,
    gotoSettingsButtonAction : () -> Unit,
    gotoStartScreenButtonAction : () -> Unit,
){

    val context = LocalContext.current

    val openSnackbar = remember { mutableStateOf(false) }



    Scaffold(
        topBar = _topBar ,
        content = {
            CSProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MediumTurquoise
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .verticalScroll(state = rememberScrollState())
                            .padding(30.dp)
                    ){

                        helpOption(
                            optionText = "Logging Purchases",
                            optionIconId = R.drawable.money_sign,
                            _onClick = gotoLoggingHelpButtonAction
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        helpOption(
                            optionText = "Categorising Purchases",
                            optionIconId = R.drawable.categorisation_sign,
                            _onClick = gotoCategorisationHelpButtonAction
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        helpOption(
                            optionText = "Graphing",
                            optionIconId = R.drawable.graph_sign,
                            _onClick = gotoGraphingHelpButtonAction
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        helpOption(
                            optionText = "Settings",
                            optionIconId = R.drawable.ic_baseline_settings_50,
                            _onClick = gotoSettingsButtonAction
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        TextButton(
                            onClick = gotoStartScreenButtonAction,
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
                            Text("< Go back to Start Page", style = MaterialTheme.typography.button, textAlign = TextAlign.Center)
                        }

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

@Composable
fun helpOption(optionText : String, optionIconId : Int, _onClick : () -> Unit){
    Row(

    ){
        Icon(
            painter = painterResource(id = optionIconId),
            contentDescription = "Icon for $optionText help option.",
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterVertically)
        )

        TextButton(
            onClick = _onClick,
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
            Text(optionText, style = MaterialTheme.typography.button, textAlign = TextAlign.Center)
        }
    }
}