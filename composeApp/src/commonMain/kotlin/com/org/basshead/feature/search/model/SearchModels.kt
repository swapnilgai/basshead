package com.org.basshead.feature.search.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.FestivalItemState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

// Network models
@Serializable
data class SearchResult(
    val id: String,
    val name: String,
    val description: String?,
    val location: String,
    val start_time: String, // ISO string from Supabase
    val end_time: String, // ISO string from Supabase
    val image_url: String?,
    val status: String,
    val participant_count: Int,
)

// Filter models
@Immutable
data class SearchFilters(
    val statusOptions: List<StatusFilter> = listOf(
        StatusFilter("upcoming", "Upcoming", true),
        StatusFilter("ongoing", "Ongoing", true),
    ),
) {
    val selectedStatuses: List<String>
        get() = statusOptions.filter { it.isSelected }.map { it.value }
}

@Immutable
data class StatusFilter(
    val value: String,
    val label: String,
    val isSelected: Boolean,
)

// Mapping extensions
fun SearchResult.toFestivalItemState(): FestivalItemState {
    // Parse the ISO timestamp and create a readable date string
    val startInstant = Instant.parse(start_time)
    val startDateTime = startInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dateString = "${startDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${startDateTime.dayOfMonth}, ${startDateTime.year}"

    return FestivalItemState(
        id = id,
        name = name,
        description = description,
        location = location,
        startTime = start_time,
        endTime = end_time,
        imageUrl = image_url,
        createdAt = "", // Not needed for search results
        status = status,
        totalParticipants = participant_count,
        totalHeadbangs = null, // Search results don't include user-specific data
        userRank = null,
        dateString = dateString,
        userJoined = false, // Would need to be determined separately if needed
    )
}
