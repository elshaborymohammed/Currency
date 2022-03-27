package com.test.data.model

import com.google.gson.annotations.Expose

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out T : Any> {

    object Loading : Resource<Void>()

    data class Success<out T : Any>(@Expose val data: T) : Resource<T>()

    data class Error(@Expose val error: ErrorMessage) : Resource<ErrorMessage>()

    data class Failure(val throwable: Throwable) : Resource<Throwable>()

    object Void : Resource<Void>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${error}]"
            else -> "Failure"
        }
    }
}