package com.test.data.model

import com.google.gson.annotations.Expose

data class ErrorMessage(
    @Expose
    val code: Int,
    @Expose
    val type: String,
    @Expose
    val info: String
)