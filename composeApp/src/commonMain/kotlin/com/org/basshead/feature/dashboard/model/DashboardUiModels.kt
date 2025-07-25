package com.org.basshead.feature.dashboard.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    val dateString: String, // Added precomputed date string
)

@Immutable
data class UserProfileState(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val isAdmin: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

@Immutable
data class FestivalItemState(
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
    val totalHeadbangs: Long? = null, // Only for user festivals
    val userRank: Int? = null, // Only for user festivals
    val dateString: String? = null, // Only for suggestions
    val userJoined: Boolean = false,
)

@Immutable
data class DashBoardUiState(
    val joinedFestivals: List<FestivalItemState> = emptyList(),
    val suggestionFestivals: List<FestivalItemState> = emptyList(),
    val profile: UserProfileState? = null,
    val dailyHeadbangs: List<DailyHeadbangState> = emptyList(),
    val totalHeadbangs: Long = 0L, // Direct value instead of computed
    val hasMoreSuggestions: Boolean = true,
    val lastSeenId: String? = null,
    // Device sync state
    val isDeviceConnected: Boolean = false,
    val isSyncing: Boolean = false,
)

// Mapping extension
fun UserProfile.toUiModel() = UserProfileState(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    isAdmin = isAdmin,
    createdAt = createdAt,
    updatedAt = updatedAt,
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
    dateString = formatFestivalDateRange(startTime, endTime), // Compute date string here
)

fun UserFestival.toFestivalItemState(): FestivalItemState = FestivalItemState(
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
    totalHeadbangs = totalHeadbangs,
    userRank = userRank,
    dateString = formatFestivalDateRange(startTime, endTime),
    userJoined = true,
)

fun FestivalSuggestion.toFestivalItemState(): FestivalItemState = FestivalItemState(
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
    totalHeadbangs = null,
    userRank = null,
    dateString = formatFestivalDateRange(startTime, endTime),
    userJoined = false,
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
    dateString = "",
)

fun formatFestivalDateRange(start: String, end: String): String {
    return try {
        val timeZone = TimeZone.UTC
        val startDate = Instant.parse(start).toLocalDateTime(timeZone)
        val endDate = Instant.parse(end).toLocalDateTime(timeZone)
        val startMonth = startDate.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val endMonth = endDate.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val startStr = buildString {
            append(startDate.dayOfMonth)
            append(" ")
            append(startMonth)
            append(" ")
            append(startDate.year)
            append(", ")
            append(if (startDate.hour < 10) "0" else "")
            append(startDate.hour)
            append(":")
            append(if (startDate.minute < 10) "0" else "")
            append(startDate.minute)
        }
        val endStr = buildString {
            append(endDate.dayOfMonth)
            append(" ")
            append(endMonth)
            append(" ")
            append(endDate.year)
            append(", ")
            append(if (endDate.hour < 10) "0" else "")
            append(endDate.hour)
            append(":")
            append(if (endDate.minute < 10) "0" else "")
            append(endDate.minute)
        }
        "$startStr – $endStr"
    } catch (e: Exception) {
        "$start - $end"
    }
}

fun isFestivalStarted(startTime: String): Boolean {
    return try {
        val now = Clock.System.now()
        val festivalStart = Instant.parse(startTime)
        now >= festivalStart
    } catch (e: Exception) {
        false // If we can't parse the date, assume not started for safety
    }
}
