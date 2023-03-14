package com.example.csproject.ViewModels

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import com.example.csproject.data.HelpScreenOptionState
import com.example.csproject.data.TransactionLog
import com.example.csproject.data.TransactionLogsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransactionLogViewModel : ViewModel()  {

    private val _uiState = MutableStateFlow(TransactionLogsState())
    val uiState: StateFlow<TransactionLogsState> = _uiState.asStateFlow()

    fun getTransaction(transactionName : String) : TransactionLog? {

        val transactionList = _uiState.value.transactions
        for(t in transactionList){
            if(t.name.equals(transactionName)) return t
        }
        return null
    }

    fun addTransaction(transaction: TransactionLog){
        _uiState.update { currentState ->
            currentState.transactions.add(transaction)
            currentState.copy()
        }
    }

    fun editTransactionName(transaction : TransactionLog, newName: String) {
        _uiState.update { currentState ->
            currentState.transactions.get(currentState.copy().transactions.indexOf(transaction)).name = newName
            currentState.copy()
        }
    }

    fun editTransactionAmount(transaction : TransactionLog, value: Double) {
        _uiState.update { currentState ->
            currentState.transactions.get(currentState.copy().transactions.indexOf(transaction)).amount = value
            currentState.copy()
        }
    }


}