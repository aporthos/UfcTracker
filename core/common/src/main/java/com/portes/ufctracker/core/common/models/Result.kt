package com.portes.ufctracker.core.common.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val exception: String?) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it.message)) }
}

// TODO: Update later with Flows
fun <T, R> Response<T>.callApi(transform: (T) -> R): Result<R> {
    return try {
        if (isSuccessful) {
            Result.Success(transform(body()!!))
        } else {
            Result.Error(message())
        }
    } catch (e: Throwable) {
        Result.Error(e.message)
    }
}
