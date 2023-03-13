package com.example.csproject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.csproject.ui.theme.CSProjectTheme
import com.example.csproject.ui.theme.MediumTurquoise


@Composable
fun StartScreen(
    _topBar : @Composable () -> Unit,
    gotoAppButtonAction : () -> Unit,
    gotoOperatingInstructionsActivityButtonAction : () -> Unit,
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
                            .padding(30.dp),
                    ){
                        Text(
                            "Money\nMind",
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TextButton(
                            onClick = gotoAppButtonAction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(30))
                                .border(width = 3.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(30))
                        ){
                            Text("Go Past Onboarding ->", style = MaterialTheme.typography.button)
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        TextButton(
                            onClick = gotoOperatingInstructionsActivityButtonAction,
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterHorizontally)
                                .border(width = 2.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(30))
                                .padding(5.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, contentColor = Color.Blue)
                        ){
                            Text("Operating Instructions", style = MaterialTheme.typography.button, color = Color.Blue)
                        }
                    }
                }
            }
        },
        bottomBar = {
            CSProjectTheme {
                createSnackbar(
                    text = "Going to Operating Instructions Activity",
                    showDialog = openSnackbar
                )
            }
        }
    )
}

@Composable
fun createSnackbar(text: String, showDialog: MutableState<Boolean>){
    if(showDialog.value){
        Snackbar(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(Alignment.Center)
            ){
                Text(text, style = MaterialTheme.typography.subtitle1)

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        showDialog.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss", style = MaterialTheme.typography.caption)
                }
            }
        }
    }
}

