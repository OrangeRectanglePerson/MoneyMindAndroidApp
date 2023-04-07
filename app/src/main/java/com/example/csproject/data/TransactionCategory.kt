package com.example.csproject.data

import androidx.compose.ui.graphics.Color
import com.example.csproject.ui.Extras.ColorSerializer
import com.example.csproject.ui.Extras.TransactionCategorySerializer
import kotlinx.serialization.Serializable

@Serializable(with = TransactionCategorySerializer::class)
data class TransactionCategory(var name : String, var color : @Serializable(with = ColorSerializer::class) Color){
    fun equals(other: TransactionCategory?): Boolean {
        if (other != null) {
            if(this.name == other.name) return true
        }
        return false
    }
}
