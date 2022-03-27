package com.test.module.convert.domain.entity

import com.google.gson.annotations.Expose

data class Convert(
    @Expose
    val from: String = "USD",
    @Expose
    val to: String = "EUR",
    @Expose
    val amount: Float = 25f
)