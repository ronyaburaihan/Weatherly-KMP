package org.envobyte.weatherforecast.permissions

interface PermissionHandler {
    suspend fun requestMicrophonePermission(): PermissionStatus
    suspend fun isMicrophonePermissionGranted(): Boolean
}

interface LocationPermissionHandler {
    suspend fun requestLocationPermission(): PermissionStatus
    suspend fun isLocationPermissionGranted(): Boolean
}