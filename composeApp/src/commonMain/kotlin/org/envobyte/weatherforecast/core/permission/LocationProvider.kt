package org.envobyte.weatherforecast.core.permission

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

expect class LocationProvider(context:Any?) {
    suspend fun getCurrentLocation(): LocationData?
}