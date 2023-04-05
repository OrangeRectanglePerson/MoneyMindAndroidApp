package com.example.csproject.ViewModels

import androidx.lifecycle.ViewModel
import com.example.csproject.data.GraphScreenState
import com.example.csproject.data.HelpScreenOptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GraphScreenViewModel  : ViewModel() {

    companion object{
        //type of graph to use
        val BAR_GRAPH = false; val PIE_CHART = true;

        //value to use for graphing
        val PERCENTAGE_OF_NUMBER = 0; val PERCENTAGE_OF_MONEY = 1; val AMOUNT_OF_MONEY = 2;
    }

    private val _uiState = MutableStateFlow(GraphScreenState())
    val uiState: StateFlow<GraphScreenState> = _uiState.asStateFlow()

    fun setGraphScreenGraphType(newGraphType : Boolean){
        //use constants defined in companion object
        //if out of range, default to lowest value
        _uiState.update { currentState ->
            currentState.copy(
                graphType = newGraphType
            )
        }
    }

    fun setGraphScreenValueType(newValueType : Int){
        //use constants defined in companion object
        //if out of range, default to lowest value
        _uiState.update { currentState ->
            currentState.copy(
                unitType = newValueType
            )
        }
    }
}