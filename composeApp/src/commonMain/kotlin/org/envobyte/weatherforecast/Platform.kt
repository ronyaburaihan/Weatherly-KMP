package org.envobyte.weatherforecast

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform