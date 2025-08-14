package org.envobyte.weatherforecast.presentation.screen.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.usecase.SaveFirstTimeUseCase

class IntroViewModel(
    private val saveFirstTimeUseCase: SaveFirstTimeUseCase,
) : ViewModel() {
    fun updateFirstTime() {
        viewModelScope.launch {
            saveFirstTimeUseCase(false)
        }
    }
}