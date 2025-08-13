package org.envobyte.weatherforecast.domain.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null
)