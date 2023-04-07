package com.example.csproject.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.csproject.ViewModels.GraphScreenViewModel
import java.util.*

data class GraphScreenState (
    val graphType : Boolean = GraphScreenViewModel.BAR_GRAPH,
    val unitType: Int = GraphScreenViewModel.PERCENTAGE_OF_NUMBER,
    val filterByCategories : Boolean = false,
    var categoryWhitelist : SnapshotStateList<TransactionCategory> = mutableStateListOf(),

    //money time graph
    val moneyTimeMode : Boolean = false,
    var lowerDateLimit : Date = Date(1672506061000),
    var upperDateLimit : Date = Calendar.getInstance().time,
){

}