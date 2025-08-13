package org.envobyte.weatherforecast.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.core.permission.LocationManager
import org.envobyte.weatherforecast.domain.model.LocationData
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

    fun checkLocationPermission(locationManager: LocationManager) {
        viewModelScope.launch {
            val granted = getPermissionStatusUseCase(locationManager)
            _uiState.value = _uiState.value.copy(
                needLocationPermission = !granted
            )
        }
    }

    fun requestCurrentLocation(locationManager: LocationManager) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            val locationData = getCurrentLocationUseCase(locationManager)
            println("Location Data: $locationData")
            if (locationData != null) {
                loadWeatherDataAndLocationName(locationData)
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "Location data is null"
                )
            }
        }
    }

    private fun loadWeatherDataAndLocationName(locationData: LocationData) {
        loadWeatherData(locationData)
        loadLocationName(locationData)
    }

    fun requestLocationPermission(locationManager: LocationManager) {
        println("Requesting location permission")
        viewModelScope.launch {
            val permissionState = requestLocationPermissionUseCase(locationManager)
            println("Permission State: $permissionState")

            when (permissionState) {
                PermissionState.GRANTED -> {
                    _uiState.value = _uiState.value.copy(needLocationPermission = false)
                }

                PermissionState.NOT_DETERMINED -> {
                    _uiState.value = _uiState.value.copy(needLocationPermission = true)
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        needLocationPermission = true,
                        error = "Location permission denied you need enable it in settings"
                    )
                }
            }
        }
    }

    private fun loadLocationName(locationData: LocationData) {
        viewModelScope.launch {
            val locationName = getLocationNameUseCase(locationData).getOrNull()
            _uiState.value = _uiState.value.copy(
                locationName = locationName
            )
        }
    }

    private fun loadWeatherData(locationData: LocationData) {
        viewModelScope.launch {
            getWeatherDataUseCase(locationData).fold(
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