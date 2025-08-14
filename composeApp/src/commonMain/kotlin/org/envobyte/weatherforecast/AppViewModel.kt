package org.envobyte.weatherforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.usecase.GetFirstTimeUseCase
import org.envobyte.weatherforecast.presentation.navigation.Screen

class AppViewModel(
    private val getFirstTimeUseCase: GetFirstTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val isFirstTime = getFirstTimeUseCase.invoke().firstOrNull()
            _uiState.value = _uiState.value.copy(
                initialScreen = if (isFirstTime == true) Screen.Intro else Screen.Home
            )
        }
    }
}

data class AppState(
    val initialScreen: Screen? = null
)