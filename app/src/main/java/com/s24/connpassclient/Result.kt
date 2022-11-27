package com.s24.connpassclient

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable as Parcelable

@Parcelize
data class Result (
    val events: List<Event>,
    @Json(name = "results_returned") val resultsReturned: Int,
    @Json(name = "results_start") val resultsStart: Int
) : Parcelable