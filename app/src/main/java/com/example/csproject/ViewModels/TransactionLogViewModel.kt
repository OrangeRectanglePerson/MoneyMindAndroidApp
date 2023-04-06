package com.example.csproject.ViewModels

import androidx.lifecycle.ViewModel
import com.example.csproject.data.TransactionCategory
import com.example.csproject.data.TransactionLog
import com.example.csproject.data.TransactionLogsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class TransactionLogViewModel() : ViewModel()  {

    private val _uiState = MutableStateFlow(TransactionLogsState())
    val uiState: StateFlow<TransactionLogsState> = _uiState.asStateFlow()

    init {
        val dummyTransactionLog0 : TransactionLog = TransactionLog("Dummy Transaction 0", 1.00, Calendar.getInstance())
        dummyTransactionLog0.categories.add(TransactionCategoryViewModel.dummyCategory0)
        val dummyTransactionLog1 : TransactionLog = TransactionLog("Dummy Transaction 1", 10.00, Calendar.getInstance())
        dummyTransactionLog1.categories.add(TransactionCategoryViewModel.dummyCategory0)
        dummyTransactionLog1.categories.add(TransactionCategoryViewModel.dummyCategory1)
        val dummyTransactionLog2 : TransactionLog = TransactionLog("Dummy Transaction 2", 100.00, Calendar.getInstance())
        dummyTransactionLog2.categories.add(TransactionCategoryViewModel.dummyCategory0)
        dummyTransactionLog2.categories.add(TransactionCategoryViewModel.dummyCategory1)
        dummyTransactionLog2.categories.add(TransactionCategoryViewModel.dummyCategory2)

        addTransaction(dummyTransactionLog0)
        addTransaction(dummyTransactionLog1)
        addTransaction(dummyTransactionLog2)
    }

    fun getTransaction(transactionName : String) : TransactionLog? {

        val transactionList = _uiState.value.transactions
        for(t in transactionList){
            if(t.name.equals(transactionName)) return t
        }
        return null
    }

    fun addTransaction(transaction: TransactionLog) : Boolean{
        return if(getTransaction(transaction.name) == null) {
            _uiState.update { currentState ->
                currentState.transactions.add(transaction)
                currentState.copy()
            }
            true //added
        } else {
            false
        } // not added

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

    fun addCategoryToTransaction(transaction : TransactionLog, category: TransactionCategory) : Boolean{
        return if(!transaction.categories.contains(category)) {
            _uiState.update { currentState ->
                currentState.transactions.get(currentState.copy().transactions.indexOf(transaction)).categories.add(
                    category
                )
                currentState.copy()
            }
            true //added
        } else {
            false //not added since category already exists
        }
    }

    fun removeCategoryFromTransaction(transaction : TransactionLog, category: TransactionCategory){
        _uiState.update { currentState ->
            currentState.transactions.get(currentState.copy().transactions.indexOf(transaction)).categories.remove(category)
            currentState.copy()
        }
    }

    fun saveTransactions() {
        //val transactionsJSON = Json.encodeToString(uiState.value.transactions)

    }


}