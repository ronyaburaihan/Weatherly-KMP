package org.envobyte.weatherforecast.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.usecase.GetFirstTimeUseCase
import org.envobyte.weatherforecast.domain.usecase.GetWeatherDataUseCase
import org.envobyte.weatherforecast.domain.usecase.SaveFirstTimeUseCase

class HomeViewModel(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getFirstTimeUseCase: GetFirstTimeUseCase,
    private val saveFirstTimeUseCase: SaveFirstTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadWeatherData()
        observeFirstLaunch()
    }

    fun refreshWeather() {
        loadWeatherData()
    }

    private fun observeFirstLaunch() {
        viewModelScope.launch {
            val isFirst = getFirstTimeUseCase().first()
            if (isFirst) {
                // First app launch: mark as no longer first for next time
                saveFirstTimeUseCase(false)
                _uiState.value = _uiState.value.copy(firstLaunchCompleted = false)
            } else {
                // Subsequent launches
                _uiState.value = _uiState.value.copy(firstLaunchCompleted = true)
            }
        }
    }

    private fun loadWeatherData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                needsLocationPermission = false
            )

            getWeatherDataUseCase().fold(
                onSuccess = { weatherData ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherData = weatherData,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val weatherData: WeatherData? = null,
    val error: String? = null,
    val needsLocationPermission: Boolean = false,
    val firstLaunchCompleted: Boolean = false
)