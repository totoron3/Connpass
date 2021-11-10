package com.s24.connpassclient

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    @Json(name = "event_id") val eventId: Int? = null,
    val title: String? = null,
    val catch: String? = null,
    val description: String? = null,
    @Json(name = "event_url") val eventUrl: String? = null,
    @Json(name = "hash_tag") val hashTag: String? = null,
    @Json(name = "started_at") val startedAt: String? = null,
    @Json(name = "ended_at") val endedAt: String? = null,
    val limit: Int?,
    @Json(name = "event_type") val eventType: String? = null,
    val address: String? = null,
    val place: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    @Json(name = "owner_id") val ownerId: Int? = null,
    @Json(name = "owner_nickname") val ownerNickname: String? = null,
    @Json(name = "owner_display_name") val ownerDisplayName: String? = null,
    @Json(name = "accepted") val accepted: Int?,
    @Json(name = "waiting") val waiting: Int?,
    @Json(name = "updated_at") val updatedAt: String? = null
) : Parcelable