package com.example.csproject.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.csproject.ViewModels.GraphScreenViewModel

data class GraphScreenState (
    val graphType : Boolean = GraphScreenViewModel.BAR_GRAPH,
    val unitType: Int = GraphScreenViewModel.PERCENTAGE_OF_NUMBER,
    var filterByCategories : SnapshotStateList<TransactionCategory> = mutableStateListOf()
)