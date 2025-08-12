package org.envobyte.weatherforecast.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.usecase.GetLocationNameUseCase
import org.envobyte.weatherforecast.domain.usecase.GetWeatherDataUseCase

class HomeViewModel(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadWeatherData()
        loadLocationName()
        //checkLocationPermissionAndLoadWeather()
    }

    private fun loadLocationName() {
        viewModelScope.launch {
            val locationName = getLocationNameUseCase(
                lat = 22.8373,
                lon = 89.5400
            ).getOrNull()
            _uiState.value = _uiState.value.copy(
                locationName = locationName
            )
        }
    }

    fun refreshWeather() {
        loadWeatherData()
    }

    /*fun requestLocationPermission() {
        viewModelScope.launch {
            val granted = locationService.requestLocationPermission()
            if (granted) {
                loadWeatherData()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Location permission is required to get weather data"
                )
            }
        }
    }

    private fun checkLocationPermissionAndLoadWeather() {
        if (locationService.hasLocationPermission()) {
            loadWeatherData()
        } else {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                needsLocationPermission = true
            )
        }
    }*/

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
    val locationName: String? = null,
    val error: String? = null,
    val needsLocationPermission: Boolean = false
)