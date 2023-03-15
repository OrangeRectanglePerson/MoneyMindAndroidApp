package com.example.csproject.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class TransactionCategoriesState (var categories : SnapshotStateList<TransactionCategory> = mutableStateListOf())