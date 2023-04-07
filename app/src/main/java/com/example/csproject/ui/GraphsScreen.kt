package com.example.csproject.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csproject.ViewModels.GraphScreenViewModel
import com.example.csproject.ViewModels.TransactionCategoryViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.data.GraphScreenState
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.ui.theme.*
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.text.SimpleDateFormat
import java.util.*

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
                            if(graphScreenState.collectAsState().value.moneyTimeMode){
                                graphTitle =
                                    "Money Logged Per Day (from ${SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(graphScreenState.collectAsState().value.lowerDateLimit)} to ${SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(graphScreenState.collectAsState().value.upperDateLimit)})"
                            }
                            else {
                                when (graphScreenState.collectAsState().value.unitType) {
                                    GraphScreenViewModel.PERCENTAGE_OF_NUMBER -> graphTitle =
                                        "Percentages of how many transactions are in each category"
                                    GraphScreenViewModel.PERCENTAGE_OF_MONEY -> graphTitle =
                                        "Percentages of how much money is logged under each category"
                                    GraphScreenViewModel.AMOUNT_OF_MONEY -> graphTitle =
                                        "Amount of how much money is logged under each category"
                                    else -> graphTitle = "Graph"
                                }
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
                                if(graphScreenState.collectAsState().value.moneyTimeMode){
                                    LineChart(
                                        lineChartData = getMoneyTimeLineGraphData(
                                            transactions = transactionLogsState.collectAsState().value.transactions,
                                            categories = transactionCategoriesState.collectAsState().value.categories,
                                            lowerDateLimit = graphScreenState.collectAsState().value.lowerDateLimit,
                                            upperDateLimit = graphScreenState.collectAsState().value.upperDateLimit,
                                        ),
                                        // Optional properties.
                                        modifier = Modifier,
                                        animation = simpleChartAnimation(),
                                        pointDrawer = FilledCircularPointDrawer(),
                                        lineDrawer = SolidLineDrawer(),
                                        xAxisDrawer = me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer(),
                                        yAxisDrawer = me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer(),
                                        horizontalOffset = 5f
                                    )
                                }
                                else {
                                    if (graphScreenState.collectAsState().value.graphType == GraphScreenViewModel.BAR_GRAPH) {
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
                                                    if (graphScreenState.value.unitType == GraphScreenViewModel.AMOUNT_OF_MONEY) String.format(
                                                        "$%.2f",
                                                        it
                                                    )
                                                    else String.format("%.1f%%", it)
                                                }
                                            ),
                                            labelDrawer = me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer(
                                                drawLocation = SimpleLabelDrawer.DrawLocation.Outside,
                                                labelTextSize = (20 * (3.0 / barChartData.bars.size)).sp,
                                            )
                                        )
                                    } else if (graphScreenState.collectAsState().value.graphType == GraphScreenViewModel.PIE_CHART) {
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



                //editing graph dialog
                if(editGraphDialog) {
                    Toast.makeText(context, "edit graph button klcik", Toast.LENGTH_SHORT).show()


                    GraphSettingsDialog(
                        current = graphScreenState.collectAsState().value,
                        onDismiss = { editGraphDialog = false },
                        onPositiveClick =  {
                            graphScreenViewModel.setGraphScreenGraphType(it.graphType)
                            graphScreenViewModel.setGraphScreenUnitType(it.unitType)

                            graphScreenViewModel.setGraphScreenMoneyTimeGraphMode(it.moneyTimeMode)
                            graphScreenViewModel.setGraphScreenLowerDateLimit(it.lowerDateLimit)
                            graphScreenViewModel.setGraphScreenUpperDateLimit(it.upperDateLimit)

                            editGraphDialog = false
                        }
                    )
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
        var totalMoneyLogged = 0.0
        for(t in transactions) totalMoneyLogged += t.amount
        for (c in categories) {
            var totalMoneyLoggedUnderCategory = 0.0
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

fun getMoneyTimeLineGraphData(
    transactions : List<TransactionLog>,
    categories : List<TransactionCategory>,
    lowerDateLimit : Date,
    upperDateLimit : Date
) : LineChartData{
    var listOfPoints : ArrayList<LineChartData.Point> = ArrayList()
    var date = Calendar.getInstance()
    date.time = lowerDateLimit
    var UDL = Calendar.getInstance()
    UDL.time = upperDateLimit

    do {
        var totalMoneyLoggedThisDay = 0.0
        for(t in transactions){
            val tDate = t.date
            if(
                date.get(Calendar.DATE) == tDate.get(Calendar.DATE)
                && date.get(Calendar.MONTH) == tDate.get(Calendar.MONTH)
                && date.get(Calendar.YEAR) == tDate.get(Calendar.YEAR)
            ) { totalMoneyLoggedThisDay += t.amount }
        }
        listOfPoints.add(LineChartData.Point(totalMoneyLoggedThisDay.toFloat(),""))
        Log.d("lol", String.format("%s = %.2f",SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date.time), totalMoneyLoggedThisDay))
        date.add(Calendar.DATE,1)
    } while (
        date.get(Calendar.DATE) != UDL.get(Calendar.DATE)
        || date.get(Calendar.MONTH) != UDL.get(Calendar.MONTH)
        || date.get(Calendar.YEAR) != UDL.get(Calendar.YEAR)
    )

    //again one more time to include upper limit
    var totalMoneyLoggedThisDay = 0.0
    for(t in transactions){
        val tDate = t.date
        if(
            date.get(Calendar.DATE) == tDate.get(Calendar.DATE)
            && date.get(Calendar.MONTH) == tDate.get(Calendar.MONTH)
            && date.get(Calendar.YEAR) == tDate.get(Calendar.YEAR)
        ) { totalMoneyLoggedThisDay += t.amount }
    }
    listOfPoints.add(LineChartData.Point(totalMoneyLoggedThisDay.toFloat(),""))
    Log.d("lol", String.format("%s = %.2f",SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date.time), totalMoneyLoggedThisDay))

    return LineChartData(
        points = listOfPoints,
        startAtZero = true
    )
}

@Composable
fun GraphSettingsDialog(
    current : GraphScreenState,
    onDismiss: (newGSS : GraphScreenState) -> Unit,
    onPositiveClick: (newGSS : GraphScreenState) -> Unit
){

    var graphType by rememberSaveable { mutableStateOf(current.graphType) }
    var unitType by rememberSaveable { mutableStateOf(current.unitType) }
    var filterByCategories by rememberSaveable { mutableStateOf(current.filterByCategories) }

    var categoryWhiteList = ArrayList<TransactionCategory>()
    for(c in current.categoryWhitelist) categoryWhiteList.add(c)

    var isMoneyTimeGraph by rememberSaveable { mutableStateOf(current.moneyTimeMode) }
    val lowerCalendar = Calendar.getInstance()
    lowerCalendar.time = current.lowerDateLimit
    var lowerDateLimit by rememberSaveable { mutableStateOf(lowerCalendar) }
    val upperCalendar = Calendar.getInstance()
    upperCalendar.time = current.upperDateLimit
    var upperDateLimit by rememberSaveable { mutableStateOf(upperCalendar) }

    val SDF = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    var openLowerLimitDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var openUpperLimitDatePickerDialog by rememberSaveable { mutableStateOf(false) }


    CSProjectTheme() {
        Dialog(
            onDismissRequest = {
                onDismiss(
                    GraphScreenState(
                        graphType = graphType,
                        unitType = unitType,
                        moneyTimeMode = isMoneyTimeGraph,
                        lowerDateLimit = lowerDateLimit.time,
                        upperDateLimit = upperDateLimit.time,
                    )
                )
            },
        ) {
            Surface(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                elevation = 8.dp,
                color = DarkCyan.copy(alpha = 0.9f)
            ) {
                Column(
                    Modifier
                        .background(Color.Transparent)
                        .verticalScroll(enabled = true, state = rememberScrollState())
                        .padding(15.dp),
                ) {



                    Column(modifier = Modifier

                    ) {
                        val focusManager = LocalFocusManager.current

                        Row(){
                            Column(
                                Modifier
                                    .padding(end = 5.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Graph Type",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .width(IntrinsicSize.Min)
                                        .align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.h3,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }


                            Column(
                                Modifier
                                    .padding(start = 5.dp)
                                    .align(Alignment.CenterVertically),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                TextButton(
                                    shape = RoundedCornerShape(30),
                                    onClick = {
                                        if(isMoneyTimeGraph) isMoneyTimeGraph = false
                                        graphType = GraphScreenViewModel.PIE_CHART
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            color = LibreOfficeBlue,
                                            shape = RoundedCornerShape(30)
                                        )
                                        .border(
                                            width = 5.dp,
                                            color = chartSelectorBorderButtonColor(
                                                isMoneyTimeGraph = isMoneyTimeGraph,
                                                isGraphType = graphType == GraphScreenViewModel.PIE_CHART
                                            ),
                                            shape = RoundedCornerShape(30)
                                        )
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        "Pie Chart",
                                        style = MaterialTheme.typography.button,
                                        color = Color.Black,
                                        modifier = Modifier
                                    )
                                }
                                TextButton(
                                    shape = RoundedCornerShape(30),
                                    onClick = {
                                        if(isMoneyTimeGraph) isMoneyTimeGraph = false
                                        graphType = GraphScreenViewModel.BAR_GRAPH
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            color = LibreOfficeBlue,
                                            shape = RoundedCornerShape(30)
                                        )
                                        .border(
                                            width = 5.dp,
                                            color = chartSelectorBorderButtonColor(
                                                isMoneyTimeGraph = isMoneyTimeGraph,
                                                isGraphType = graphType == GraphScreenViewModel.BAR_GRAPH
                                            ),
                                            shape = RoundedCornerShape(30)
                                        )
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        "Bar Chart",
                                        style = MaterialTheme.typography.button,
                                        color = Color.Black,
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Divider(
                            color = Purple500,
                            modifier = Modifier
                                .fillMaxWidth()  //fill the max height
                                .height(3.dp)
                        )
                        Spacer(Modifier.height(10.dp))


                        Row(){
                            Column(
                                Modifier
                                    .padding(end = 5.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Graph Units",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .width(IntrinsicSize.Min)
                                        .align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.h3,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }


                            Column(
                                Modifier
                                    .padding(start = 5.dp)
                                    .align(Alignment.CenterVertically),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                TextButton(
                                    shape = RoundedCornerShape(30),
                                    onClick = {
                                        if(isMoneyTimeGraph) isMoneyTimeGraph = false
                                        unitType = GraphScreenViewModel.PERCENTAGE_OF_NUMBER
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            color = LibreOfficeBlue,
                                            shape = RoundedCornerShape(30)
                                        )
                                        .border(
                                            width = 5.dp,
                                            color = chartSelectorBorderButtonColor(
                                                isMoneyTimeGraph = isMoneyTimeGraph,
                                                isGraphType = unitType == GraphScreenViewModel.PERCENTAGE_OF_NUMBER
                                            ),
                                            shape = RoundedCornerShape(30)
                                        )
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        "num%",
                                        style = MaterialTheme.typography.button,
                                        color = Color.Black,
                                        modifier = Modifier
                                    )
                                }
                                TextButton(
                                    shape = RoundedCornerShape(30),
                                    onClick = {
                                        if(isMoneyTimeGraph) isMoneyTimeGraph = false
                                        unitType = GraphScreenViewModel.PERCENTAGE_OF_MONEY
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            color = LibreOfficeBlue,
                                            shape = RoundedCornerShape(30)
                                        )
                                        .border(
                                            width = 5.dp,
                                            color = chartSelectorBorderButtonColor(
                                                isMoneyTimeGraph = isMoneyTimeGraph,
                                                isGraphType = unitType == GraphScreenViewModel.PERCENTAGE_OF_MONEY
                                            ),
                                            shape = RoundedCornerShape(30)
                                        )
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        "\$%",
                                        style = MaterialTheme.typography.button,
                                        color = Color.Black,
                                    )
                                }
                                TextButton(
                                    shape = RoundedCornerShape(30),
                                    onClick = {
                                        if(isMoneyTimeGraph) isMoneyTimeGraph = false
                                        unitType = GraphScreenViewModel.AMOUNT_OF_MONEY
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            color = LibreOfficeBlue,
                                            shape = RoundedCornerShape(30)
                                        )
                                        .border(
                                            width = 5.dp,
                                            color = chartSelectorBorderButtonColor(
                                                isMoneyTimeGraph = isMoneyTimeGraph,
                                                isGraphType = unitType == GraphScreenViewModel.AMOUNT_OF_MONEY
                                            ),
                                            shape = RoundedCornerShape(30)
                                        )
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        "\$amt",
                                        style = MaterialTheme.typography.button,
                                        color = Color.Black,
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Divider(
                            color = Purple500,
                            modifier = Modifier
                                .fillMaxWidth()  //fill the max height
                                .height(3.dp)
                        )
                        Spacer(Modifier.height(10.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            TextButton(
                                shape = RoundedCornerShape(30),
                                onClick = { isMoneyTimeGraph = !isMoneyTimeGraph },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .background(
                                        color = LibreOfficeBlue,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = if (isMoneyTimeGraph) FireBrick else MaterialTheme.colors.primaryVariant,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    "Money-Time Graph Mode",
                                    style = MaterialTheme.typography.button,
                                    color = Color.Black,
                                    modifier = Modifier
                                )
                            }

                            TextButton(
                                shape = RoundedCornerShape(30),
                                onClick = { openLowerLimitDatePickerDialog = true },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .background(
                                        color = LibreOfficeBlue,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = chartSelectorBorderButtonColor(
                                            isMoneyTimeGraph = isMoneyTimeGraph,
                                            isGraphType = graphType == GraphScreenViewModel.PIE_CHART
                                        ),
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    String.format("Lower Date Limit:%n%s", SDF.format(lowerDateLimit.time)),
                                    style = MaterialTheme.typography.button,
                                    color = Color.Black,
                                    modifier = Modifier
                                )
                            }

                            TextButton(
                                shape = RoundedCornerShape(30),
                                onClick = { openUpperLimitDatePickerDialog = true },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .background(
                                        color = LibreOfficeBlue,
                                        shape = RoundedCornerShape(30)
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = chartSelectorBorderButtonColor(
                                            isMoneyTimeGraph = isMoneyTimeGraph,
                                            isGraphType = graphType == GraphScreenViewModel.PIE_CHART,
                                        ),
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    String.format("Upper Date Limit:%n%s", SDF.format(upperDateLimit.time)),
                                    style = MaterialTheme.typography.button,
                                    color = Color.Black,
                                    modifier = Modifier
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Divider(
                            color = Purple500,
                            modifier = Modifier
                                .fillMaxWidth()  //fill the max height
                                .height(3.dp)
                        )
                        Spacer(Modifier.height(10.dp))

                    }

                    Spacer(modifier = Modifier.height(5.dp))


                    //.......................................................................
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
                            .wrapContentSize(align = Alignment.Center),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onPositiveClick(
                                    GraphScreenState(
                                        graphType = graphType,
                                        unitType = unitType,
                                        moneyTimeMode = isMoneyTimeGraph,
                                        lowerDateLimit = lowerDateLimit.time,
                                        upperDateLimit = upperDateLimit.time,
                                    )
                                )
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
                        ) {
                            Text(
                                "OK",
                                style = MaterialTheme.typography.button,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                        TextButton(
                            onClick = {
                                onDismiss(
                                    GraphScreenState(
                                        graphType = graphType,
                                        unitType = unitType,
                                        moneyTimeMode = isMoneyTimeGraph,
                                        lowerDateLimit = lowerDateLimit.time,
                                        upperDateLimit = upperDateLimit.time,
                                    )
                                )
                            },
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
                    }
                }
            }
        }

        if(openLowerLimitDatePickerDialog){
            OpenDatePickerDialog(
                listener = { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    lowerDateLimit.set(Calendar.YEAR, mYear)
                    lowerDateLimit.set(Calendar.MONTH, mMonth)
                    lowerDateLimit.set(Calendar.DAY_OF_MONTH, mDayOfMonth)

                    if(lowerDateLimit.time.after(upperDateLimit.time)){
                        lowerDateLimit.set(Calendar.YEAR, upperDateLimit.get(Calendar.YEAR))
                        lowerDateLimit.set(Calendar.MONTH, upperDateLimit.get(Calendar.MONTH))
                        lowerDateLimit.set(Calendar.DAY_OF_MONTH, upperDateLimit.get(Calendar.DAY_OF_MONTH)-1)
                    }

                    openLowerLimitDatePickerDialog = false
                }
            )
        }

        if(openUpperLimitDatePickerDialog){
            OpenDatePickerDialog(
                listener = { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    upperDateLimit.set(Calendar.YEAR, mYear)
                    upperDateLimit.set(Calendar.MONTH, mMonth)
                    upperDateLimit.set(Calendar.DAY_OF_MONTH, mDayOfMonth)

                    if(upperDateLimit.time.before(lowerDateLimit.time)){
                        upperDateLimit.set(Calendar.YEAR, lowerDateLimit.get(Calendar.YEAR))
                        upperDateLimit.set(Calendar.MONTH, lowerDateLimit.get(Calendar.MONTH))
                        upperDateLimit.set(Calendar.DAY_OF_MONTH, lowerDateLimit.get(Calendar.DAY_OF_MONTH)+1)
                    }

                    openUpperLimitDatePickerDialog = false
                }
            )
        }

    }
}

@Preview(showBackground = false)
@Composable
fun previewGSDialog(){
    GraphSettingsDialog(current = GraphScreenState(), onDismiss = {}, onPositiveClick = {})
}


//extras
@Composable
fun chartSelectorBorderButtonColor(isMoneyTimeGraph : Boolean, isGraphType : Boolean) : Color {
    if(isMoneyTimeGraph) return MaterialTheme.colors.primaryVariant
    else {
        if(isGraphType) return FireBrick
        else return MaterialTheme.colors.primaryVariant
    }
}

@Composable
fun OpenDatePickerDialog(listener : DatePickerDialog.OnDateSetListener){
    val now = Calendar.getInstance()
    DatePickerDialog(
        LocalContext.current,
        listener
        , now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
    ).show()
}
