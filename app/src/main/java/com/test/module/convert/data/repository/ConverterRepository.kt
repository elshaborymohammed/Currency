package com.test.module.convert.data.repository

import com.test.module.convert.data.api.ConvertAPI
import com.test.module.convert.domain.entity.Convert
import retrofit2.Retrofit
import javax.inject.Inject

class ConverterRepository @Inject constructor(
    private val retrofit: Retrofit
) {
    suspend fun latest(base: String, symbols: String) =
        retrofit.create(ConvertAPI::class.java).latest(base, symbols)
}