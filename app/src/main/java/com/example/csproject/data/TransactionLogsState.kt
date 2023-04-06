package com.example.csproject.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

//@kotlinx.serialization.Serializable
data class TransactionLogsState (var transactions :  SnapshotStateList<TransactionLog> = mutableStateListOf())