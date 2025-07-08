package com.org.basshead.feature.search.presentation

import com.org.basshead.feature.search.interactor.SearchInteractor
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

// Utility function to sort festivals by start date (upcoming first)
private fun List<com.org.basshead.feature.dashboard.model.FestivalItemState>.sortedByStartDate(): List<com.org.basshead.feature.dashboard.model.FestivalItemState> {
    return this.sortedWith { festival1, festival2 ->
        try {
            // Try to parse as ISO instant first
            val time1 = try {
                Instant.parse(festival1.startTime)
            } catch (e: Exception) {
                // If that fails, just compare as strings
                return@sortedWith festival1.startTime.compareTo(festival2.startTime)
            }
            
            val time2 = try {
                Instant.parse(festival2.startTime)
            } catch (e: Exception) {
                return@sortedWith festival1.startTime.compareTo(festival2.startTime)
            }
            
            time1.compareTo(time2)
        } catch (e: Exception) {
            // If all parsing fails, maintain original order
            0
        }
    }
}

sealed interface SearchActions {
    data class OnSearchQueryChanged(val query: String) : SearchActions
    data class OnSearchSubmitted(val query: String) : SearchActions
    data object OnSearchCleared : SearchActions
    data object LoadMoreResults : SearchActions
    data object LoadMoreSuggestions : SearchActions
    data object ToggleFilters : SearchActions
    data class OnStatusFilterChanged(val statusValue: String, val isSelected: Boolean) : SearchActions
    data class OnLocationFilterChanged(val location: String) : SearchActions
    data object ApplyFilters : SearchActions
    data class OnRecentSearchClicked(val query: String) : SearchActions
    data object ClearSearchHistory : SearchActions
    data class OnFestivalClicked(val festivalId: String) : SearchActions
    data class JoinFestival(val festivalId: String) : SearchActions
    data class ViewLeaderboard(val festivalId: String) : SearchActions
    data object Refresh : SearchActions
}

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : BaseViewModel<SearchUiState>(SearchUiState()) {

    private var searchJob: Job? = null
    private var loadMoreJob: Job? = null
    private val pageSize = 3 // Production page size for better UX

    init {
        loadInitialData()
    }

    fun onAction(action: SearchActions) {
        when (action) {
            is SearchActions.OnSearchQueryChanged -> onSearchQueryChanged(action.query)
            is SearchActions.OnSearchSubmitted -> onSearchSubmitted(action.query)
            is SearchActions.OnSearchCleared -> onSearchCleared()
            is SearchActions.LoadMoreResults -> loadMoreResults()
            is SearchActions.LoadMoreSuggestions -> loadMoreSuggestions()
            is SearchActions.ToggleFilters -> toggleFilters()
            is SearchActions.OnStatusFilterChanged -> onStatusFilterChanged(action.statusValue, action.isSelected)
            is SearchActions.OnLocationFilterChanged -> onLocationFilterChanged(action.location)
            is SearchActions.ApplyFilters -> applyFilters()
            is SearchActions.OnRecentSearchClicked -> onRecentSearchClicked(action.query)
            is SearchActions.ClearSearchHistory -> clearSearchHistory()
            is SearchActions.OnFestivalClicked -> onFestivalClicked(action.festivalId)
            is SearchActions.JoinFestival -> joinFestival(action.festivalId)
            is SearchActions.ViewLeaderboard -> viewLeaderboard(action.festivalId)
            is SearchActions.Refresh -> refresh()
        }
    }

    private fun loadInitialData() {
        baseViewModelScope.launch {
            // Set initial loading state for suggestions
            setContent(SearchUiState(isLoadingMore = true))
            
            val recentSearches = searchInteractor.getRecentSearches()
            val defaultStatusFilters = listOf("upcoming", "ongoing")
            val suggestionFestivals = searchInteractor.searchFestivals(
                query = "",
                statusFilters = defaultStatusFilters,
                locationFilter = null,
                limit = pageSize,
                lastSeenId = null // Start with no cursor
            )

            setContent(
                SearchUiState(
                    suggestionFestivals = suggestionFestivals.sortedByStartDate(),
                    recentSearches = recentSearches,
                    hasMoreSuggestions = suggestionFestivals.size == pageSize, // Exactly pageSize means there might be more
                    lastSeenId = suggestionFestivals.lastOrNull()?.id,
                    selectedStatusFilters = defaultStatusFilters.toSet(),
                    appliedStatusFilters = defaultStatusFilters.toSet(), // Set applied filters
                    suggestionsLastSeenId = suggestionFestivals.lastOrNull()?.id, // Initialize suggestions cursor
                    isLoadingMore = false
                )
            )
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val currentState = getContent()
        setContent(currentState.copy(searchQuery = query))
        
        searchJob?.cancel()
        if (query.trim().isNotEmpty()) {
            searchJob = baseViewModelScope.launch {
                delay(500) // Debounce
                performSearch(query.trim(), resetResults = true)
            }
        } else {
            setContent(currentState.copy(
                searchResults = emptyList(),
                hasSearched = false,
                hasMoreResults = true,
                lastSeenId = null // Reset search cursor
            ))
        }
    }

    private fun onSearchSubmitted(query: String) {
        if (query.trim().isNotEmpty()) {
            searchJob?.cancel()
            performSearch(query.trim(), resetResults = true)
            saveSearchQuery(query.trim())
        }
    }

    private fun onSearchCleared() {
        searchJob?.cancel()
        setContent(
            getContent().copy(
                searchQuery = "",
                searchResults = emptyList(),
                hasSearched = false,
                hasMoreResults = true,
                lastSeenId = null // Reset search cursor
            )
        )
    }

    private fun loadMoreResults() {
        val currentState = getContent()
        
        // Prevent multiple simultaneous requests
        if (currentState.canLoadMore && !currentState.isLoadingMore) {
            performSearch(
                query = currentState.searchQuery,
                resetResults = false
            )
        }
    }

    private fun loadMoreSuggestions() {
        loadMoreJob?.cancel()
        loadMoreJob = baseViewModelScope.launch {
            delay(300) // Debounce
            loadMoreSuggestionsInternal()
        }
    }

    private fun loadMoreSuggestionsInternal() {
        val currentState = getContent()
        if (!currentState.hasMoreSuggestions || currentState.isLoadingMore) return
        
        setContent(currentState.copy(isLoadingMore = true))

        baseViewModelScope.launch {
            val newSuggestions = searchInteractor.searchFestivals(
                query = "",
                statusFilters = currentState.appliedStatusFilters.sorted(), // Use sorted() directly on Set
                locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                limit = pageSize,
                lastSeenId = currentState.suggestionsLastSeenId // Use cursor-based pagination
            )

            val filteredNewSuggestions = newSuggestions.filter { newItem ->
                currentState.suggestionFestivals.none { it.id == newItem.id }
            }

            setContent(currentState.copy(
                suggestionFestivals = (currentState.suggestionFestivals + filteredNewSuggestions).sortedByStartDate(),
                hasMoreSuggestions = newSuggestions.size == pageSize, // Exactly pageSize means there might be more
                lastSeenId = filteredNewSuggestions.lastOrNull()?.id ?: currentState.lastSeenId,
                isLoadingMore = false,
                suggestionsLastSeenId = newSuggestions.lastOrNull()?.id, // Update cursor to last item from backend
            ))
        }
    }

    private fun toggleFilters() {
        val currentState = getContent()
        
        if (currentState.showFilters) {
            // Closing filters - revert any unapplied changes
            setContent(currentState.copy(
                showFilters = false,
                selectedStatusFilters = currentState.appliedStatusFilters,
                locationFilter = currentState.appliedLocationFilter
            ))
        } else {
            // Opening filters - keep current state
            setContent(currentState.copy(showFilters = true))
        }
    }

    private fun onStatusFilterChanged(statusValue: String, isSelected: Boolean) {
        val currentState = getContent()
        val currentFilters = currentState.selectedStatusFilters.toMutableSet()
        
        if (statusValue == "all") {
            if (isSelected) {
                // Select "all" - backend handles this correctly
                currentFilters.clear()
                currentFilters.add("all")
            } else {
                // Deselect "all" - default to upcoming and ongoing
                currentFilters.clear()
                currentFilters.addAll(setOf("upcoming", "ongoing"))
            }
        } else {
            // Remove "all" if selecting specific statuses
            currentFilters.remove("all")
            if (isSelected) {
                currentFilters.add(statusValue)
            } else {
                currentFilters.remove(statusValue)
            }
            
            // If no statuses selected, default to upcoming and ongoing
            if (currentFilters.isEmpty()) {
                currentFilters.addAll(setOf("upcoming", "ongoing"))
            }
        }
        
        // Only update the selected filters, don't apply automatically
        setContent(currentState.copy(selectedStatusFilters = currentFilters))
    }

    private fun onLocationFilterChanged(location: String) {
        val currentState = getContent()
        // Only update the location filter, don't apply automatically
        setContent(currentState.copy(locationFilter = location))
    }

    private fun applyFilters() {
        val currentState = getContent()
        
        // Apply filters and close filter panel
        setContent(currentState.copy(
            showFilters = false,
            appliedStatusFilters = currentState.selectedStatusFilters,
            appliedLocationFilter = currentState.locationFilter,
            lastSeenId = null,
            suggestionsLastSeenId = null,
            hasMoreResults = true,
            hasMoreSuggestions = true
        ))
        
        // Refresh data with new filters
        if (currentState.searchQuery.isNotEmpty()) {
            performSearch(currentState.searchQuery, resetResults = true)
        } else {
            refreshSuggestions()
        }
    }

    /**
     * Apply filters only if they have changed from the previously applied state.
     * This prevents unnecessary API calls and maintains cache efficiency.
     * If no changes are detected, only closes the filter panel without making API calls.
     */
    private fun refreshSuggestions() {
        baseViewModelScope.launch {
            val currentState = getContent()
            setContent(currentState.copy(isLoadingMore = true))

            val filteredSuggestions = searchInteractor.searchFestivals(
                query = "",
                statusFilters = currentState.appliedStatusFilters.sorted(),
                locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                limit = pageSize,
                lastSeenId = null
            )

            setContent(currentState.copy(
                suggestionFestivals = filteredSuggestions.sortedByStartDate(),
                hasMoreSuggestions = filteredSuggestions.size == pageSize,
                lastSeenId = filteredSuggestions.lastOrNull()?.id,
                isLoadingMore = false,
                suggestionsLastSeenId = filteredSuggestions.lastOrNull()?.id,
            ))
        }
    }

    private fun onRecentSearchClicked(query: String) {
        setContent(getContent().copy(searchQuery = query))
        performSearch(query, resetResults = true)
    }

    private fun clearSearchHistory() {
        baseViewModelScope.launch {
            searchInteractor.clearSearchHistory()
            setContent(getContent().copy(recentSearches = emptyList()))
        }
    }

    private fun onFestivalClicked(festivalId: String) {
        navigate("festival/$festivalId")
    }

    private fun joinFestival(festivalId: String) {
        baseViewModelScope.launch {
            // TODO: Implement festival joining logic via interactor
            // searchInteractor.joinFestival(festivalId)

            // Refresh the data to update the UI
            refresh()
        }
    }

    private fun viewLeaderboard(festivalId: String) {
        navigate("leaderboard/$festivalId")
    }

    private fun performSearch(query: String, resetResults: Boolean) {
        baseViewModelScope.launch {
            val currentState = getContent()
            
            val currentLastSeenId = if (resetResults) null else currentState.searchResults.lastOrNull()?.id
            
            setContent(currentState.copy(
                isSearching = if (resetResults) true else false, // Only show main loading for new searches
                isLoadingMore = if (resetResults) false else true, // Show pagination loading for load more
                lastSeenId = if (resetResults) null else currentState.lastSeenId
            ))

            val searchResults = searchInteractor.searchFestivals(
                query = query,
                statusFilters = currentState.appliedStatusFilters.sorted(), // Use sorted() directly on Set
                locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                limit = pageSize,
                lastSeenId = currentLastSeenId // Use cursor-based pagination
            )

            val newState = if (resetResults) {
                currentState.copy(
                    searchResults = searchResults.sortedByStartDate(),
                    isSearching = false,
                    isLoadingMore = false,
                    hasSearched = true,
                    hasMoreResults = searchResults.size == pageSize, // Exactly pageSize means there might be more
                    lastSeenId = searchResults.lastOrNull()?.id // Update cursor to last item from results
                )
            } else {
                // Filter out duplicates when appending results
                val filteredNewResults = searchResults.filter { newItem ->
                    currentState.searchResults.none { it.id == newItem.id }
                }
                // Sort the combined results to maintain date order
                val combinedResults = (currentState.searchResults + filteredNewResults).sortedByStartDate()
                currentState.copy(
                    searchResults = combinedResults,
                    isSearching = false,
                    isLoadingMore = false,
                    hasMoreResults = searchResults.size == pageSize, // Exactly pageSize means there might be more
                    lastSeenId = searchResults.lastOrNull()?.id // Update cursor to last item from results
                )
            }
            
            setContent(newState)
        }
    }

    private fun refresh() {
        loadMoreJob?.cancel()
        searchJob?.cancel()
        loadInitialData()
    }

    private fun saveSearchQuery(query: String) {
        baseViewModelScope.launch {
            searchInteractor.saveSearchQuery(query)
            // Refresh recent searches
            val recentSearches = searchInteractor.getRecentSearches()
            setContent(getContent().copy(recentSearches = recentSearches))
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
        searchJob?.cancel()
    }
}
