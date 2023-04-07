package com.example.moneymind.data

//import com.example.csproject.ui.Extras.SnapshotListTransactionCategorySerializer
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.moneymind.ui.Extras.SnapshotListTransactionCategorySerializer
import com.example.moneymind.ui.Extras.TransactionCategoriesStateSerializer

@kotlinx.serialization.Serializable (with = TransactionCategoriesStateSerializer::class)
data class TransactionCategoriesState (
    var categories :
    @kotlinx.serialization.Serializable(with = SnapshotListTransactionCategorySerializer::class) SnapshotStateList<TransactionCategory> = mutableStateListOf()
) : java.io.Serializable