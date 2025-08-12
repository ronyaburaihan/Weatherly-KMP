package org.envobyte.weatherforecast.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoLocationResponse(
    @SerialName("display_name") val displayName: String,
    val address: Address?
)

@Serializable
data class Address(
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val country: String? = null
)