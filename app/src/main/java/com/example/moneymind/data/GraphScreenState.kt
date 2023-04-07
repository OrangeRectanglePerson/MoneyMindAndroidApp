package com.example.moneymind.data

import com.example.moneymind.ViewModels.GraphScreenViewModel
import java.util.*

data class GraphScreenState (
    val graphType : Boolean = GraphScreenViewModel.BAR_GRAPH,
    val unitType: Int = GraphScreenViewModel.PERCENTAGE_OF_NUMBER,
    val filterByCategories : Boolean = false,
    var categoryWhitelist : ArrayList<TransactionCategory> = ArrayList(),

    //money time graph
    val moneyTimeMode : Boolean = false,
    var lowerDateLimit : Date = Date(1672506061000),
    var upperDateLimit : Date = Calendar.getInstance().time,
) : java.io.Serializable