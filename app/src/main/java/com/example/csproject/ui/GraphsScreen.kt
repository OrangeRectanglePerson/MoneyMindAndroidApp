package com.example.csproject.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.theme.CSProjectTheme
import me.bytebeats.views.charts.AxisLabelFormatter
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun GraphsScreen(
    _topBar : @Composable () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
    transactionCategoryViewModel: TransactionCategoryViewModel
){

    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    val transactionCategoriesState by remember{ mutableStateOf(transactionCategoryViewModel.uiState) }
    var editGraphDialog by remember{ mutableStateOf(false) }


    Scaffold(
        topBar = _topBar ,
        //floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            //no fab in grpahing scren
            //FloatingActionButton(
            //    onClick = {
            //        if(!showTransactionCreationDialog) showTransactionCreationDialog = true
            //    },
            //    backgroundColor = SpringGreen
            //) {
            //    Icon(
            //        painter = painterResource(R.drawable.money_sign),
            //        contentDescription = "",
            //        tint = DarkCyan,
            //    )
            //}
        },
        content = {
            CSProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Column(
                        Modifier
                            .background(Color.Transparent)
                            .verticalScroll(enabled = true, state = rememberScrollState())
                            .padding(10.dp)
                    ) {

                        TextButton(
                            onClick = { editGraphDialog = true },
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
                        ) {
                            Text(
                                "Edit Graph",
                                style = MaterialTheme.typography.button,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .padding(10.dp)
                                .height(300.dp)
                        ) {
                            BarChart(
                                barChartData = getBarChartDataPerCategory(
                                    transactions = transactionLogsState.collectAsState().value.transactions,
                                    categories = transactionCategoriesState.collectAsState().value.categories
                                ),
                                // Optional properties.
                                modifier = Modifier,
                                animation = simpleChartAnimation(),
                                barDrawer = SimpleBarDrawer(),
                                xAxisDrawer = me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer(),
                                yAxisDrawer = me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer(
                                    drawLabelEvery = 3
                                ),
                                labelDrawer = me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer(
                                    drawLocation = SimpleLabelDrawer.DrawLocation.XAxis,
                                    labelTextSize = 20.sp,
                                )
                            )
                        }


                    }
                }

                //logging dialog
                if(editGraphDialog) {
                    Toast.makeText(context, "edit graph button klcik", Toast.LENGTH_SHORT).show()
                    editGraphDialog = false
                }


            }
        }
    )
}


fun getBarChartDataPerCategory(transactions : List<TransactionLog>, categories : List<TransactionCategory>) : BarChartData{
    var listOfBars : ArrayList<BarChartData.Bar> = ArrayList()
    for(c in categories){
        var numofitems = 0
        for(t in transactions) {
            if (t.categories.contains(c)) numofitems++
        }
        listOfBars.add(
            BarChartData.Bar(
                value = ((numofitems.toDouble()/transactions.size.toDouble())*100).toFloat(),
                color = c.color,
                label = String.format("%.2f%%", ((numofitems.toDouble()/transactions.size.toDouble())*100))
            )
        )
    }
    return BarChartData(
        bars = listOfBars,
        startAtZero = true,
        maxBarValue = 100f,
    )
}