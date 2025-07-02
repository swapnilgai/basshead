package com.org.basshead.feature.search.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.FestivalItemState

@Immutable
data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<FestivalItemState> = emptyList(),
    val isSearching: Boolean = false,
)
