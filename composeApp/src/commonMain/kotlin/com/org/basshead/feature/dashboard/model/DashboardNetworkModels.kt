package com.org.basshead.feature.dashboard.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyHeadbang(
    val date: String, // ISO date format
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("has_festival")
    val hasFestival: Boolean,
)

@Serializable
data class UserFestival(
    val id: String,
    val name: String,
    val description: String?,
    val location: String,
    @SerialName("start_time")
    val startTime: String, // ISO datetime
    @SerialName("end_time")
    val endTime: String, // ISO datetime
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("created_at")
    val createdAt: String,
    val status: String,
    @SerialName("total_headbangs")
    val totalHeadbangs: Long,
    @SerialName("user_rank")
    val userRank: Int?,
    @SerialName("total_participants")
    val totalParticipants: Int,
)

@Serializable
data class FestivalSuggestion(
    val id: String,
    val name: String,
    val description: String?,
    val location: String,
    @SerialName("start_time") val startTime: String,
    @SerialName("end_time") val endTime: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("created_at") val createdAt: String,
    val status: String,
    @SerialName("total_participants") val totalParticipants: Int,
)


@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("is_admin") val isAdmin: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)