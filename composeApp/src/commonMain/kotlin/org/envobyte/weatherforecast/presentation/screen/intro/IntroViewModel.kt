package org.envobyte.weatherforecast.presentation.screen.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.usecase.GetFirstTimeUseCase
import org.envobyte.weatherforecast.domain.usecase.SaveFirstTimeUseCase

class IntroViewModel(
    private val saveFirstTimeUseCase: SaveFirstTimeUseCase,
    private val getFirstTimeUseCase: GetFirstTimeUseCase
) : ViewModel() {

    private val _isFirstTime = MutableStateFlow<Boolean?>(null)
    val isFirstTime: StateFlow<Boolean?> = _isFirstTime

    init {
        viewModelScope.launch {
            getFirstTimeUseCase.invoke().collect {
                _isFirstTime.value = it
            }
        }
    }

    fun updateFirstTime() {
        viewModelScope.launch {
            saveFirstTimeUseCase(false)
        }
    }
}