package com.org.basshead.feature.search.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.FestivalItemState

@Immutable
data class SearchUiState(
    val searchQuery: String = "",
    val suggestionFestivals: List<FestivalItemState> = emptyList(),
    val isSearching: Boolean = false,
    val hasMoreSuggestions: Boolean = true,
    val lastSeenId: String? = null,
    val isLoadingMore: Boolean = false,
)
