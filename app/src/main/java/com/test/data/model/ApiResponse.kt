package com.test.data.model

import com.google.gson.annotations.Expose

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
data class ApiResponse<out T : Any>(
    @Expose val success: Boolean,
    @Expose val data: T?,
    @Expose val error: ErrorMessage?
)