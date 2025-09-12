package com.org.basshead.design.constants

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.org.basshead.design.theme.BassheadTheme

/**
 * Festival-related constants and mappings
 * Centralized location for festival status colors and other festival-specific constants
 */
object FestivalConstants {

    /**
     * Festival status values
     */
    object Status {
        const val ONGOING = "ongoing"
        const val UPCOMING = "upcoming"
        const val COMPLETED = "completed"
        const val INACTIVE = "inactive"
    }

    /**
     * Get festival status background color based on status string
     */
    @Composable
    fun getStatusColor(status: String): Color {
        return when (status.lowercase()) {
            Status.ONGOING -> BassheadTheme.colors.festivalActive
            Status.UPCOMING -> BassheadTheme.colors.festivalUpcoming
            Status.COMPLETED -> BassheadTheme.colors.festivalPast
            else -> BassheadTheme.colors.festivalInactive
        }
    }

    /**
     * Get festival status text color based on status string
     */
    @Composable
    fun getStatusTextColor(status: String): Color {
        return when (status.lowercase()) {
            Status.ONGOING -> BassheadTheme.colors.onFestivalActive
            Status.UPCOMING -> BassheadTheme.colors.onFestivalUpcoming
            Status.COMPLETED -> BassheadTheme.colors.onFestivalPast
            else -> BassheadTheme.colors.onFestivalInactive
        }
    }

    /**
     * Get both status colors as a pair (background, text)
     */
    @Composable
    fun getStatusColors(status: String): Pair<Color, Color> {
        return Pair(
            getStatusColor(status),
            getStatusTextColor(status)
        )
    }
}
