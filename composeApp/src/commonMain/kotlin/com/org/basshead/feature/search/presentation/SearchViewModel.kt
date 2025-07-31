package com.org.basshead.feature.search.presentation

import com.org.basshead.feature.search.interactor.SearchInteractor
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.navigation.Route
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
            setLoading()

            val defaultStatusFilters = listOf("upcoming", "ongoing")
            val suggestionFestivals = searchInteractor.searchFestivals(
                query = "",
                statusFilters = defaultStatusFilters,
                locationFilter = null,
                limit = pageSize,
                lastSeenId = null, // Start with no cursor
            )

            setContent(
                SearchUiState(
                    suggestionFestivals = suggestionFestivals.sortedByStartDate(),
                    recentSearches = emptyList(), // Remove recent searches functionality
                    hasMoreSuggestions = suggestionFestivals.size == pageSize, // Exactly pageSize means there might be more
                    lastSeenId = suggestionFestivals.lastOrNull()?.id,
                    selectedStatusFilters = defaultStatusFilters.toSet(),
                    appliedStatusFilters = defaultStatusFilters.toSet(), // Set applied filters
                    suggestionsLastSeenId = suggestionFestivals.lastOrNull()?.id, // Initialize suggestions cursor
                    isLoadingMore = false,
                ),
            )
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val currentState = getContent()

        // Implement distinctUntilChanged behavior - only proceed if query actually changed
        if (currentState.searchQuery == query) {
            return
        }

        setContent(currentState.copy(searchQuery = query))

        searchJob?.cancel()
        if (query.trim().isNotEmpty()) {
            searchJob = baseViewModelScope.launch {
                delay(500) // Debounce
                performSearch(query.trim(), resetResults = true)
            }
        } else {
            setContent(
                currentState.copy(
                    searchResults = emptyList(),
                    hasSearched = false,
                    hasMoreResults = true,
                    lastSeenId = null, // Reset search cursor
                ),
            )
        }
    }

    private fun onSearchSubmitted(query: String) {
        if (query.trim().isNotEmpty()) {
            searchJob?.cancel()
            performSearch(query.trim(), resetResults = true)
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
                lastSeenId = null, // Reset search cursor
            ),
        )
    }

    private fun loadMoreResults() {
        val currentState = getContent()

        // Prevent multiple simultaneous requests
        if (currentState.canLoadMore && !currentState.isLoadingMore) {
            performSearch(
                query = currentState.searchQuery,
                resetResults = false,
            )
        }
    }

    private fun loadMoreSuggestions() {
        // Cancel any existing load more job
        loadMoreJob?.cancel()

        val currentState = getContent()
        // Early return if already loading or no more data
        if (!currentState.hasMoreSuggestions || currentState.isLoadingMore) return

        // Reduce debounce time and start loading immediately for better responsiveness
        loadMoreJob = baseViewModelScope.launch {
            delay(100) // Reduced debounce for better responsiveness
            loadMoreSuggestionsInternal()
        }
    }

    private fun loadMoreSuggestionsInternal() {
        val currentState = getContent()
        // Double-check state after debounce delay
        if (!currentState.hasMoreSuggestions || currentState.isLoadingMore) return

        setContent(currentState.copy(isLoadingMore = true))

        baseViewModelScope.launch {
            try {
                val newSuggestions = searchInteractor.searchFestivals(
                    query = "",
                    statusFilters = currentState.appliedStatusFilters.sorted(),
                    locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                    limit = pageSize,
                    lastSeenId = currentState.suggestionsLastSeenId,
                )

                // Filter out duplicates
                val filteredNewSuggestions = newSuggestions.filter { newItem ->
                    currentState.suggestionFestivals.none { it.id == newItem.id }
                }

                // Get the updated state in case it changed during the API call
                val latestState = getContent()

                setContent(
                    latestState.copy(
                        suggestionFestivals = (latestState.suggestionFestivals + filteredNewSuggestions).sortedByStartDate(),
                        hasMoreSuggestions = newSuggestions.size == pageSize,
                        lastSeenId = newSuggestions.lastOrNull()?.id ?: latestState.lastSeenId,
                        isLoadingMore = false,
                        suggestionsLastSeenId = newSuggestions.lastOrNull()?.id ?: latestState.suggestionsLastSeenId,
                    ),
                )
            } catch (e: Exception) {
                // Handle error case - stop loading state
                val latestState = getContent()
                setContent(latestState.copy(isLoadingMore = false))
            }
        }
    }

    private fun toggleFilters() {
        val currentState = getContent()

        if (currentState.showFilters) {
            // Closing filters - revert any unapplied changes
            setContent(
                currentState.copy(
                    showFilters = false,
                    selectedStatusFilters = currentState.appliedStatusFilters,
                    locationFilter = currentState.appliedLocationFilter,
                ),
            )
        } else {
            // Opening filters - keep current state
            setContent(currentState.copy(showFilters = true))
        }
    }

    private fun onStatusFilterChanged(statusValue: String, isSelected: Boolean) {
        val currentState = getContent()
        val currentFilters = currentState.selectedStatusFilters.toMutableSet()

        if (isSelected) {
            currentFilters.add(statusValue)
        } else {
            currentFilters.remove(statusValue)
        }

        // Ensure at least one filter is always selected
        if (currentFilters.isEmpty()) {
            // If user tries to deselect all, keep the current one selected
            currentFilters.add(statusValue)
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

        // Cancel any ongoing operations first
        loadMoreJob?.cancel()
        searchJob?.cancel()

        // Apply filters and close filter panel
        setContent(
            currentState.copy(
                showFilters = false,
                appliedStatusFilters = currentState.selectedStatusFilters,
                appliedLocationFilter = currentState.locationFilter,
                lastSeenId = null,
                suggestionsLastSeenId = null,
                hasMoreResults = true,
                hasMoreSuggestions = true,
                isLoadingMore = false,
                isSearching = false,
            ),
        )

        // Refresh data with new filters
        if (currentState.searchQuery.isNotEmpty()) {
            performSearch(currentState.searchQuery, resetResults = true)
        } else {
            refreshSuggestions()
        }
    }

    /**
     * Refresh suggestions with applied filters.
     * This resets pagination and loads the first page of results.
     */
    private fun refreshSuggestions() {
        // Cancel any ongoing operations
        loadMoreJob?.cancel()
        searchJob?.cancel()

        baseViewModelScope.launch {
            val currentState = getContent()
            setContent(
                currentState.copy(
                    isLoadingMore = true,
                    suggestionFestivals = emptyList(), // Clear existing results
                    suggestionsLastSeenId = null,
                ),
            )

            try {
                val filteredSuggestions = searchInteractor.searchFestivals(
                    query = "",
                    statusFilters = currentState.appliedStatusFilters.sorted(),
                    locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                    limit = pageSize,
                    lastSeenId = null,
                )

                setContent(
                    currentState.copy(
                        suggestionFestivals = filteredSuggestions.sortedByStartDate(),
                        hasMoreSuggestions = filteredSuggestions.size == pageSize,
                        lastSeenId = filteredSuggestions.lastOrNull()?.id,
                        isLoadingMore = false,
                        suggestionsLastSeenId = filteredSuggestions.lastOrNull()?.id,
                    ),
                )
            } catch (e: Exception) {
                // Handle error case
                setContent(
                    currentState.copy(
                        isLoadingMore = false,
                        suggestionFestivals = emptyList(),
                        hasMoreSuggestions = false,
                    ),
                )
            }
        }
    }

    private fun onRecentSearchClicked(query: String) {
        setContent(getContent().copy(searchQuery = query))
        performSearch(query, resetResults = true)
    }

    private fun clearSearchHistory() {
        // Recent searches functionality removed - this method now does nothing
        setContent(getContent().copy(recentSearches = emptyList()))
    }

    private fun onFestivalClicked(festivalId: String) {
        // Navigate to festival details
        navigate(
            destination = "${Route.FestivalDetails::class.simpleName}/$festivalId",
        )
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
        navigate("${Route.FestivalLeaderBoard::class.simpleName}/$festivalId")
    }

    private fun performSearch(query: String, resetResults: Boolean) {
        // Cancel any ongoing search operations
        searchJob?.cancel()
        loadMoreJob?.cancel()

        searchJob = baseViewModelScope.launch {
            val currentState = getContent()

            val currentLastSeenId = if (resetResults) null else currentState.searchResults.lastOrNull()?.id

            setContent(
                currentState.copy(
                    isSearching = if (resetResults) true else false, // Only show main loading for new searches
                    isLoadingMore = if (resetResults) false else true, // Show pagination loading for load more
                    lastSeenId = if (resetResults) null else currentState.lastSeenId,
                ),
            )

            val searchResults = searchInteractor.searchFestivals(
                query = query,
                statusFilters = currentState.appliedStatusFilters.sorted(),
                locationFilter = currentState.appliedLocationFilter.takeIf { it.isNotBlank() },
                limit = pageSize,
                lastSeenId = currentLastSeenId,
            )

            // Get latest state in case it changed during the API call
            val latestState = getContent()

            val newState = if (resetResults) {
                latestState.copy(
                    searchResults = searchResults.sortedByStartDate(),
                    isSearching = false,
                    isLoadingMore = false,
                    hasSearched = true,
                    hasMoreResults = searchResults.size == pageSize,
                    lastSeenId = searchResults.lastOrNull()?.id,
                )
            } else {
                // Filter out duplicates when appending results
                val filteredNewResults = searchResults.filter { newItem ->
                    latestState.searchResults.none { it.id == newItem.id }
                }
                // Sort the combined results to maintain date order
                val combinedResults = (latestState.searchResults + filteredNewResults).sortedByStartDate()
                latestState.copy(
                    searchResults = combinedResults,
                    isSearching = false,
                    isLoadingMore = false,
                    hasMoreResults = searchResults.size == pageSize,
                    lastSeenId = searchResults.lastOrNull()?.id,
                )
            }

            setContent(newState)
        }
    }

    private fun refresh() {
        // Cancel all ongoing operations
        loadMoreJob?.cancel()
        searchJob?.cancel()

        // Reset loading states
        val currentState = getContent()
        setContent(
            currentState.copy(
                isLoadingMore = false,
                isSearching = false,
                lastSeenId = null,
                suggestionsLastSeenId = null,
                hasMoreResults = true,
                hasMoreSuggestions = true,
            ),
        )

        // Reload initial data
        loadInitialData()
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
        searchJob?.cancel()
    }
}
