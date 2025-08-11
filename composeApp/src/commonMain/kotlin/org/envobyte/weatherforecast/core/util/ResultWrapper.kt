package org.envobyte.weatherforecast.core.util

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Failure(val message: String, val isUnAuthorized: Boolean = false) :
        ResultWrapper<Nothing>()
}

fun <T, R> ResultWrapper<T>.map(transform: (T) -> R): ResultWrapper<R> {
    return when (this) {
        is ResultWrapper.Success -> ResultWrapper.Success(transform(this.data))
        is ResultWrapper.Failure -> ResultWrapper.Failure(this.message)
    }
}