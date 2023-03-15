package com.example.csproject.ViewModels

import androidx.lifecycle.ViewModel
import com.example.csproject.data.TransactionCategoriesState
import com.example.csproject.data.TransactionCategory
import com.example.csproject.ui.theme.FireBrick
import com.example.csproject.ui.theme.MidnightBlue
import com.example.csproject.ui.theme.Purple500
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransactionCategoryViewModel : ViewModel()  {

    companion object{
        val dummyCategory0 : TransactionCategory = TransactionCategory("Food (dummy category)", FireBrick)
        val dummyCategory1 : TransactionCategory = TransactionCategory("Shopping (dummy category)", Purple500)
        val dummyCategory2 : TransactionCategory = TransactionCategory("Others (dummy category)", MidnightBlue)
    }

    private val _uiState = MutableStateFlow(TransactionCategoriesState())
    val uiState: StateFlow<TransactionCategoriesState> = _uiState.asStateFlow()

    init {
        addCategory(dummyCategory0)
        addCategory(dummyCategory1)
        addCategory(dummyCategory2)
    }

    fun getCategory(categoryName : String) : TransactionCategory? {

        val categoriesList = _uiState.value.categories
        for(t in categoriesList){
            if(t.name.equals(categoryName)) return t
        }
        return null
    }

    fun addCategory(category: TransactionCategory) : Boolean{
        return if(getCategory(category.name) == null) {
            _uiState.update { currentState ->
                currentState.categories.add(category)
                currentState.copy()
            }
            true //added
        } else {
            false
        } // not added
    }

}