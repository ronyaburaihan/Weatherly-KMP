package org.envobyte.weatherforecast.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.domain.model.PermissionState
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.usecase.GetCurrentLocationUseCase
import org.envobyte.weatherforecast.domain.usecase.GetLocationNameUseCase
import org.envobyte.weatherforecast.domain.usecase.GetLocationPermissionStatusUseCase
import org.envobyte.weatherforecast.domain.usecase.GetWeatherDataUseCase
import org.envobyte.weatherforecast.domain.usecase.RequestLocationPermissionUseCase

class HomeViewModel(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getPermissionStatusUseCase: GetLocationPermissionStatusUseCase,
    private val requestLocationPermissionUseCase: RequestLocationPermissionUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadWeatherData()
        loadLocationName()
        checkLocationPermission()
        //checkLocationPermissionAndLoadWeather()
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            val granted = getPermissionStatusUseCase()
            _uiState.value = _uiState.value.copy(
                needLocationPermission = !granted
            )
        }
    }

    fun requestLocationPermission() {
        println("Requesting location permission")
        viewModelScope.launch {
            val permissionState = requestLocationPermissionUseCase()
            println("Permission State: $permissionState")

            permissionState.fold(
                onSuccess = { state ->
                    when (state) {
                        PermissionState.GRANTED -> {
                            _uiState.value = _uiState.value.copy(needLocationPermission = false)
                            loadWeatherData()
                        }

                        PermissionState.NOT_DETERMINED -> {
                            // Permission needs to be handled in UI
                            _uiState.value = _uiState.value.copy(needLocationPermission = true)
                        }

                        else -> {
                            _uiState.value = _uiState.value.copy(
                                needLocationPermission = true,
                                error = "Location permission denied"
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Permission request failed"
                    )
                }
            )
        }
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
                needLocationPermission = false
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
    val needLocationPermission: Boolean? = null
)