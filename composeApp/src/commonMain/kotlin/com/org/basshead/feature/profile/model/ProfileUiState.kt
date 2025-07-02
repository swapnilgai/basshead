package com.org.basshead.feature.profile.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.DailyHeadbangState
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.UserProfileState

@Immutable
data class ProfileUiState(
    val profile: UserProfileState? = null,
    val userFestivals: List<FestivalItemState> = emptyList(),
    val dailyHeadbangs: List<DailyHeadbangState> = emptyList(),
)
