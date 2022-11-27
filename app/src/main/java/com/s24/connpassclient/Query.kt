package com.s24.connpassclient

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(
    val keyword: String? = null,
    val prefecture: String? = null,
    val date: String? = null,
    val searchCondition: Int? = null,
    var start: Int = 0
) : Parcelable
