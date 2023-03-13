package com.example.csproject.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.csproject.ui.CommonUI.makeButton
import com.example.csproject.ui.theme.*

val dialogTargetString = mutableStateOf("Dialog Not Callled Yet")

@Composable
fun TransactionListScreen(
    _topBar : @Composable () -> Unit,
){

    var timesFabPressed by remember{ mutableStateOf(0) }
    var timesButtonPressed by remember{ mutableStateOf(0) }
    val context = LocalContext.current


    Scaffold(
        topBar = _topBar ,
        //floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    timesFabPressed++
                    Toast.makeText(context, "FAB Clicked!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
        , content = {
            CSProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column{
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
                        Row{
                            val dialogTargetStringRemember by remember{ dialogTargetString }
                            val openDialog = remember { mutableStateOf(false) }
                            CreateCustomDialog(dialogTargetString, openDialog = openDialog)
                            makeButton(onclick = { openDialog.value = true }, text = "call dialog")
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier
                                .widthIn(0.dp, 200.dp)
                            ){
                                Text(dialogTargetStringRemember, fontSize = 30.sp)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CreateCustomDialog(targetString: MutableState<String>, openDialog: MutableState<Boolean>){
    if (openDialog.value) {
        CustomDialog(
            onPositiveClick = { returnedString ->
                targetString.value = returnedString
                openDialog.value = false
            },
            onNegativeClick = {
                targetString.value = "Negative"
                // confirm clicked
                openDialog.value = false
            }, onDismiss = {
                //dismissed
                openDialog.value = false
            }, modifier = Modifier
        )
    }
}

@Composable
fun CustomDialog(
    modifier : Modifier,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (returnedString : String) -> Unit
){
    var text by rememberSaveable { mutableStateOf("Text") }


    Dialog(
        onDismissRequest = onDismiss,
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
                modifier
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
                        text = "This is a dialog",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h3,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Testing Dialogs out.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body1
                    )
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        label = { Text("Label") },
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() },
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                        textStyle = MaterialTheme.typography.body1
                    )
                }
                //.......................................................................
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 5.dp)
                        .wrapContentSize(align = Alignment.Center),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .background(color = LibreOfficeBlue, shape = RoundedCornerShape(30))
                            .border(width = 2.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(30))
                            .padding(5.dp),
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.button,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                    TextButton(
                        shape = RoundedCornerShape(10.dp),
                        onClick = { onNegativeClick() },
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .background(color = FireBrick, shape = RoundedCornerShape(30))
                            .border(width = 2.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(30))
                            .padding(5.dp),
                    ) {
                        Text(
                            "Negative",
                            style = MaterialTheme.typography.button,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(top = 5.dp, bottom = 5.dp)

                        )
                    }
                    TextButton(
                        shape = RoundedCornerShape(10.dp),
                        onClick = { onPositiveClick(text) },
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .background(color = LimeGreen, shape = RoundedCornerShape(30))
                            .border(width = 2.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(30))
                            .padding(5.dp),
                    ) {
                        Text(
                            "Modify Text",
                            style = MaterialTheme.typography.button,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(top = 5.dp, bottom = 5.dp)

                        )
                    }
                }
            }
        }
    }
}

