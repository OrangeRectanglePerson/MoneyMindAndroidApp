package com.example.csproject.ViewModels

import androidx.compose.runtime.mutableStateListOf
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

class TransactionCategoryViewModel() : ViewModel()  {




    companion object{
        val dummyCategory0 : TransactionCategory = TransactionCategory("Food", FireBrick)
        val dummyCategory1 : TransactionCategory = TransactionCategory("Shopping", Purple500)
        val dummyCategory2 : TransactionCategory = TransactionCategory("Others", MidnightBlue)
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

    fun getCategoryList() : List<TransactionCategory>{
        return _uiState.value.categories
    }

    fun addCategory(category: TransactionCategory) : Boolean{
        return if(getCategory(category.name) == null) {

            _uiState.value.categories.add(category)

            true //added
        } else {
            false
        } // not added
    }

    fun changeTransactionCategoriesState(newTCS : TransactionCategoriesState?){
        _uiState.update {
            newTCS ?: it.copy(categories = mutableStateListOf(dummyCategory0, dummyCategory1, dummyCategory2))
        }
    }


}