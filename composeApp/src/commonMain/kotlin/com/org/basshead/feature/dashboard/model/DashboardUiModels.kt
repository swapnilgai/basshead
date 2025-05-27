package com.org.basshead.feature.dashboard.model

import androidx.compose.runtime.Immutable

@Immutable
data class DailyHeadbangState(
    val date: String,
    val totalCount: Int,
    val hasFestival: Boolean,
)

@Immutable
data class UserFestivalState(
    val id: String,
    val name: String,
    val description: String?,
    val location: String,
    val startTime: String,
    val endTime: String,
    val imageUrl: String?,
    val createdAt: String,
    val status: String,
    val totalHeadbangs: Long,
    val userRank: Int?,
    val totalParticipants: Int,
)

@Immutable
data class FestivalSuggestionState(
    val id: String,
    val name: String,
    val description: String?,
    val location: String,
    val startTime: String,
    val endTime: String,
    val imageUrl: String?,
    val createdAt: String,
    val status: String,
    val totalParticipants: Int,
)

// Mapping extensions
fun DailyHeadbang.toUiModel() = DailyHeadbangState(
    date = date,
    totalCount = totalCount,
    hasFestival = hasFestival,
)

fun UserFestival.toUiModel() = UserFestivalState(
    id = id,
    name = name,
    description = description,
    location = location,
    startTime = startTime,
    endTime = endTime,
    imageUrl = imageUrl,
    createdAt = createdAt,
    status = status,
    totalHeadbangs = totalHeadbangs,
    userRank = userRank,
    totalParticipants = totalParticipants,
)

fun FestivalSuggestion.toUiModel() = FestivalSuggestionState(
    id = id,
    name = name,
    description = description,
    location = location,
    startTime = startTime,
    endTime = endTime,
    imageUrl = imageUrl,
    createdAt = createdAt,
    status = status,
    totalParticipants = totalParticipants,
)

val EmptyDailyHeadbangState = DailyHeadbangState(
    date = "",
    totalCount = 0,
    hasFestival = false,
)

val EmptyUserFestivalState = UserFestivalState(
    id = "",
    name = "",
    description = null,
    location = "",
    startTime = "",
    endTime = "",
    imageUrl = null,
    createdAt = "",
    status = "",
    totalHeadbangs = 0L,
    userRank = null,
    totalParticipants = 0,
)

val EmptyFestivalSuggestionState = FestivalSuggestionState(
    id = "",
    name = "",
    description = null,
    location = "",
    startTime = "",
    endTime = "",
    imageUrl = null,
    createdAt = "",
    status = "",
    totalParticipants = 0,
)
