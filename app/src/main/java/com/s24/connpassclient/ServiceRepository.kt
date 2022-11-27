package com.s24.connpassclient

import com.s24.connpassclient.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ServiceRepository {


    private val moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://connpass.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    private val api = retrofit.create(ConnpassApi::class.java)

    suspend fun search(query: com.s24.connpassclient.Query) = (Dispatchers.IO) {

        var keyword: String = query.keyword ?: ""

        keyword = when (keyword.isBlank()) {
            true -> query.prefecture ?: ""
            else -> keyword + " ${query.prefecture ?: ""}"

        }

        when (query.searchCondition) {
            //OR
            R.id.radioButton2 -> {
                api.search(
                    keywordOr = keyword.split(" "),
                    ymd = query.date?.toIntOrNull(),
                    start = query.start
                )
            }
            //AND
            else -> {
                api.search(
                    keyword = keyword.split(" "),
                    ymd = query.date?.toIntOrNull(),
                    start = query.start
                )
            }
        }
    }
}