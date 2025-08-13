package org.envobyte.weatherforecast.core.permission

import org.envobyte.weatherforecast.domain.model.LocationData

expect class LocationProvider(context:Any?) {
    suspend fun getCurrentLocation(): LocationData?
}