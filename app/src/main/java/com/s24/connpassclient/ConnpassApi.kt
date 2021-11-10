package com.s24.connpassclient

import retrofit2.http.GET
import retrofit2.http.Query

interface ConnpassApi {

    @GET("api/v1/event/")
    suspend fun search(
        @Query("keyword") keyword: List<String>? = null,
        @Query("keyword_or") keywordOr: List<String>? = null,
        @Query("ymd") ymd: Int? = null,
        @Query("start") start: Int
    ): Result
}