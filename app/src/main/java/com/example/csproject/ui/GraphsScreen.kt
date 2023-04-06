package com.example.csproject.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.ViewModels.GraphScreenViewModel
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.theme.CSProjectTheme
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun GraphsScreen(
    _topBar : @Composable () -> Unit,
    transactionsLogViewModel : TransactionLogViewModel,
    transactionCategoryViewModel: TransactionCategoryViewModel,
    graphScreenViewModel : GraphScreenViewModel = viewModel(),
){

    val context = LocalContext.current
    val transactionLogsState by remember{ mutableStateOf(transactionsLogViewModel.uiState) }
    val transactionCategoriesState by remember{ mutableStateOf(transactionCategoryViewModel.uiState) }
    val graphScreenState by remember{ mutableStateOf(graphScreenViewModel.uiState) }
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

                    LazyColumn(
                       modifier = Modifier
                           .background(Color.Transparent)
                           .fillMaxWidth()
                           .wrapContentSize(Alignment.TopCenter),
                        contentPadding = PaddingValues(10.dp),
                    ) {

                        item{
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


                            var graphTitle : String
                            when (graphScreenState.collectAsState().value.unitType){
                                GraphScreenViewModel.PERCENTAGE_OF_NUMBER -> graphTitle = "Percentages of how many transactions are in each category"
                                GraphScreenViewModel.PERCENTAGE_OF_MONEY-> graphTitle = "Percentages of how much money is logged under each category"
                                GraphScreenViewModel.AMOUNT_OF_MONEY -> graphTitle = "Amount of how much money is logged under each category"
                                else -> graphTitle = "Graph"
                            }
                            Text(
                                text = graphTitle,
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Box(
                                modifier = Modifier
                                    .background(color = Color.Transparent)
                                    .padding(10.dp)
                                    .height(300.dp)
                            ) {
                                if(graphScreenState.collectAsState().value.graphType == GraphScreenViewModel.BAR_GRAPH) {
                                    val barChartData = getBarChartDataPerCategory(
                                        transactions = transactionLogsState.collectAsState().value.transactions,
                                        categories = transactionCategoriesState.collectAsState().value.categories,
                                        valueType = graphScreenState.collectAsState().value.unitType
                                    )
                                    BarChart(
                                        barChartData = barChartData,
                                        // Optional properties.
                                        modifier = Modifier,
                                        animation = simpleChartAnimation(),
                                        barDrawer = SimpleBarDrawer(),
                                        xAxisDrawer = me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer(),
                                        yAxisDrawer = me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer(
                                            drawLabelEvery = 3,
                                            labelValueFormatter = {
                                                if(graphScreenState.value.unitType == GraphScreenViewModel.AMOUNT_OF_MONEY) String.format("$%.2f",it)
                                                else String.format("%.1f%%",it)
                                            }
                                        ),
                                        labelDrawer = me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer(
                                            drawLocation = SimpleLabelDrawer.DrawLocation.Outside,
                                            labelTextSize = (20*(3.0/barChartData.bars.size)).sp,
                                        )
                                    )
                                } else if(graphScreenState.collectAsState().value.graphType == GraphScreenViewModel.PIE_CHART) {
                                    PieChart(
                                        pieChartData = getPieChartData(
                                            transactions = transactionLogsState.collectAsState().value.transactions,
                                            categories = transactionCategoriesState.collectAsState().value.categories,
                                            valueType = graphScreenState.collectAsState().value.unitType
                                        ),
                                        // Optional properties.
                                        modifier = Modifier,
                                        animation = simpleChartAnimation(),
                                        sliceDrawer = SimpleSliceDrawer(
                                            sliceThickness = 100f
                                        )
                                    )
                                }
                            }
                        }

                        item{
                            Text(
                                text = "Legend:",
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        items(items = transactionCategoriesState.value.categories){ category ->
                            val text : StringBuilder = java.lang.StringBuilder(category.name)
                            if(graphScreenState.collectAsState().value.graphType == GraphScreenViewModel.PIE_CHART){
                                val valueType = graphScreenState.collectAsState().value.unitType
                                var transactions = transactionLogsState.collectAsState().value.transactions
                                if(valueType == GraphScreenViewModel.PERCENTAGE_OF_NUMBER){
                                    var numofitems = 0
                                    for (t in transactions) {
                                        if (t.categories.contains(category)) numofitems++
                                    }
                                    text.append(String.format(" | %.2f%%", ((numofitems.toDouble() / transactions.size.toDouble()) * 100)))
                                } else if (valueType == GraphScreenViewModel.PERCENTAGE_OF_MONEY){
                                    var totalMoneyLogged : Double = 0.0
                                    for(t in transactions) totalMoneyLogged += t.amount
                                    var totalMoneyLoggedUnderCategory : Double = 0.0
                                    for(t in transactions){
                                        if (t.categories.contains(category)) totalMoneyLoggedUnderCategory += t.amount
                                    }
                                    text.append(String.format(" | %.2f%%", ((totalMoneyLoggedUnderCategory / totalMoneyLogged) * 100)))
                                } else if (valueType == GraphScreenViewModel.AMOUNT_OF_MONEY){
                                    var totalMoneyLoggedUnderCategory : Double = 0.0
                                    for(t in transactions){
                                        if (t.categories.contains(category)) totalMoneyLoggedUnderCategory += t.amount
                                    }
                                    text.append(String.format(" | $%.2f", totalMoneyLoggedUnderCategory))
                                }
                            }
                            Text(
                                text = text.toString(),
                                style = MaterialTheme.typography.caption,
                                color = category.color
                            )
                        }


                    }
                }



                //logging dialog
                if(editGraphDialog) {
                    Toast.makeText(context, "edit graph button klcik", Toast.LENGTH_SHORT).show()
                    editGraphDialog = false

                    if (graphScreenState.collectAsState().value.unitType == GraphScreenViewModel.AMOUNT_OF_MONEY) {
                        graphScreenViewModel.setGraphScreenValueType(GraphScreenViewModel.PERCENTAGE_OF_NUMBER)
                        graphScreenViewModel.setGraphScreenGraphType(!graphScreenState.collectAsState().value.graphType)
                    } else if ( graphScreenState.collectAsState().value.unitType == GraphScreenViewModel.PERCENTAGE_OF_MONEY){
                        graphScreenViewModel.setGraphScreenValueType(GraphScreenViewModel.AMOUNT_OF_MONEY)
                    }
                    else graphScreenViewModel.setGraphScreenValueType(GraphScreenViewModel.PERCENTAGE_OF_MONEY)
                }


            }
        }
    )
}


fun getBarChartDataPerCategory(transactions : List<TransactionLog>, categories : List<TransactionCategory>, valueType : Int) : BarChartData{
    var listOfBars : ArrayList<BarChartData.Bar> = ArrayList()
    if(valueType == GraphScreenViewModel.PERCENTAGE_OF_NUMBER) {
        for (c in categories) {
            var numofitems = 0
            for (t in transactions) {
                if (t.categories.contains(c)) numofitems++
            }
            listOfBars.add(
                BarChartData.Bar(
                    value = ((numofitems.toDouble() / transactions.size.toDouble()) * 100).toFloat(),
                    color = c.color,
                    label = String.format(
                        "%.2f%%",
                        ((numofitems.toDouble() / transactions.size.toDouble()) * 100)
                    )
                    //    .replace(Regex(".{8}")) { matchResult ->
                    //    String.format("%s%n|",matchResult.value)
                    //}
                )
            )
        }
    } else if (valueType == GraphScreenViewModel.PERCENTAGE_OF_MONEY){
        var totalMoneyLogged : Double = 0.0
        for(t in transactions) totalMoneyLogged += t.amount
        for (c in categories) {
            var totalMoneyLoggedUnderCategory : Double = 0.0
            for(t in transactions){
                if (t.categories.contains(c)) totalMoneyLoggedUnderCategory += t.amount
            }
            listOfBars.add(
                BarChartData.Bar(
                    value = ((totalMoneyLoggedUnderCategory / totalMoneyLogged) * 100).toFloat(),
                    color = c.color,
                    label = String.format(
                        "%.2f%%",
                        ((totalMoneyLoggedUnderCategory / totalMoneyLogged) * 100)
                    )
                )
            )
        }
    } else if (valueType == GraphScreenViewModel.AMOUNT_OF_MONEY){
        for (c in categories) {
            var totalMoneyLoggedUnderCategory : Double = 0.0
            for(t in transactions){
                if (t.categories.contains(c)) totalMoneyLoggedUnderCategory += t.amount
            }
            listOfBars.add(
                BarChartData.Bar(
                    value = totalMoneyLoggedUnderCategory.toFloat(),
                    color = c.color,
                    label = String.format(
                        "$%.2f",
                        totalMoneyLoggedUnderCategory
                    )
                )
            )
        }
    }
    return BarChartData(
        bars = listOfBars,
        startAtZero = true,
    )
}

fun getPieChartData(transactions : List<TransactionLog>, categories : List<TransactionCategory>, valueType : Int) : PieChartData{
    var listOfSlices : ArrayList<PieChartData.Slice> = ArrayList()
    if(valueType == GraphScreenViewModel.PERCENTAGE_OF_NUMBER) {
        for (c in categories) {
            var numofitems = 0
            for (t in transactions) {
                if (t.categories.contains(c)) numofitems++
            }
            listOfSlices.add(
                PieChartData.Slice(
                    value = ((numofitems.toDouble() / transactions.size.toDouble()) * 100).toFloat(),
                    color = c.color,
                )
            )
        }
    } else if (valueType == GraphScreenViewModel.PERCENTAGE_OF_MONEY){
        var totalMoneyLogged : Double = 0.0
        for(t in transactions) totalMoneyLogged += t.amount
        for (c in categories) {
            var totalMoneyLoggedUnderCategory : Double = 0.0
            for(t in transactions){
                if (t.categories.contains(c)) totalMoneyLoggedUnderCategory += t.amount
            }
            listOfSlices.add(
                PieChartData.Slice(
                    value = ((totalMoneyLoggedUnderCategory / totalMoneyLogged) * 100).toFloat(),
                    color = c.color,
                )
            )
        }
    } else if (valueType == GraphScreenViewModel.AMOUNT_OF_MONEY){
        for (c in categories) {
            var totalMoneyLoggedUnderCategory : Double = 0.0
            for(t in transactions){
                if (t.categories.contains(c)) totalMoneyLoggedUnderCategory += t.amount
            }
            listOfSlices.add(
                PieChartData.Slice(
                    value = totalMoneyLoggedUnderCategory.toFloat(),
                    color = c.color,
                )
            )
        }
    }
    return PieChartData(
        slices = listOfSlices
    )
}