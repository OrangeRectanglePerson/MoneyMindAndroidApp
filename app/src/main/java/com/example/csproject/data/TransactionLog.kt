package com.example.csproject.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.*
import kotlin.collections.ArrayList

data class TransactionLog (var name : String, var amount : Double, var date : Calendar, var categories : SnapshotStateList<TransactionCategory> = mutableStateListOf())
