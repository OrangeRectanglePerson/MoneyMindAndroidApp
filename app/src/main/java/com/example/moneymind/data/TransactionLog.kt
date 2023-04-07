package com.example.moneymind.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.Contextual
import java.util.*

@kotlinx.serialization.Serializable
data class TransactionLog (var name : String, var amount : Double, var date : @Contextual Calendar, var categories : @Contextual SnapshotStateList<TransactionCategory> = mutableStateListOf())
