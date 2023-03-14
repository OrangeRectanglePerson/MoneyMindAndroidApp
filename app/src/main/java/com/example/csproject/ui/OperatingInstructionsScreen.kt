package com.example.csproject.ui

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
import com.example.csproject.R
import com.example.csproject.customTitleAppBar
import com.example.csproject.ui.theme.CSProjectTheme
import com.example.csproject.ui.theme.MediumTurquoise

@Composable
fun OperatingInstructionsScreen(
    _topBar : @Composable () -> Unit,
    gotoPreviousScreenAction : () -> Unit,
    helpOption : String,
){

    val context = LocalContext.current

    val openSnackbar = remember { mutableStateOf(false) }

    Scaffold(
        topBar = _topBar,
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

                        Text("Help Text for $helpOption", style = MaterialTheme.typography.body1)

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