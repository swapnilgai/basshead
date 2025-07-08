package com.org.basshead.feature.search.model

import androidx.compose.runtime.Immutable
import com.org.basshead.feature.dashboard.model.FestivalItemState

@Immutable
data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<FestivalItemState> = emptyList(),
    val suggestionFestivals: List<FestivalItemState> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val hasMoreResults: Boolean = true,
    val hasMoreSuggestions: Boolean = true,
    val lastSeenId: String? = null, // Cursor for search results pagination
    val isLoadingMore: Boolean = false,
    val suggestionsLastSeenId: String? = null, // Separate cursor for suggestions pagination
    
    // Filters
    val selectedStatusFilters: Set<String> = setOf("upcoming", "ongoing"),
    val locationFilter: String = "",
    val showFilters: Boolean = false,
    
    // Applied filters (what's currently being used for API calls)
    val appliedStatusFilters: Set<String> = setOf("upcoming", "ongoing"),
    val appliedLocationFilter: String = "",
) {
    val canLoadMore: Boolean
        get() = hasMoreResults && !isSearching && searchResults.isNotEmpty()
        
    val shouldShowEmptyState: Boolean
        get() = hasSearched && searchResults.isEmpty() && !isSearching
        
    val shouldShowSuggestionsEmptyState: Boolean
        get() = !hasSearched && searchQuery.isEmpty() && suggestionFestivals.isEmpty() && !isLoadingMore
        
    val shouldShowRecentSearches: Boolean
        get() = !hasSearched && searchQuery.isEmpty() && recentSearches.isNotEmpty() && suggestionFestivals.isNotEmpty()
        
    val shouldShowSuggestions: Boolean
        get() = !hasSearched && searchQuery.isEmpty() && suggestionFestivals.isNotEmpty()
        
    val shouldShowSearchResults: Boolean
        get() = hasSearched && searchQuery.isNotEmpty()
        
    val hasFiltersChanged: Boolean
        get() = selectedStatusFilters != appliedStatusFilters || 
                locationFilter.trim() != appliedLocationFilter.trim()
}
