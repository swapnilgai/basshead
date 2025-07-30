package com.org.basshead.feature.festivaldetail.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.FestivalItemState

@Immutable
data class FestivalDetailUiState(
    val festival: FestivalItemState? = null,
    val isJoining: Boolean = false,
    val joinError: String? = null,
    val isRefreshing: Boolean = false,
    val userInteractionEnabled: Boolean = true,
)
