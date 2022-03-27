package com.test.module.convert.data.api

import com.test.module.convert.domain.entity.LatestResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConvertAPI {

    @GET("latest?format=1")
    suspend fun latest(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<LatestResponse>
}