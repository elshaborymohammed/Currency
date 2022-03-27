package com.test.module.convert.domain.entity

import com.google.gson.annotations.Expose
import com.test.data.model.ErrorMessage

data class LatestResponse(
    @Expose val success: Boolean,
    @Expose val base: String,
    @Expose val rates: Map<String, Double>,
    @Expose val error: ErrorMessage?
)