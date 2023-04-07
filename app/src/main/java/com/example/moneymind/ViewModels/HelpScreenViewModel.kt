package com.example.moneymind.ViewModels

import androidx.lifecycle.ViewModel
import com.example.moneymind.data.HelpScreenOptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HelpScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HelpScreenOptionState())
    val uiState: StateFlow<HelpScreenOptionState> = _uiState.asStateFlow()

    fun setHelpScreenOption(s : String){
        _uiState.update { currentState ->
            currentState.copy(
                helpOption = s
            )
        }
    }
}