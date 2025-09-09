package org.envobyte.weatherforecast.data.source.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        createBaseHttpClientConfig()
    }
}