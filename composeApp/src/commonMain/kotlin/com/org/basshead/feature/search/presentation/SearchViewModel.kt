package com.org.basshead.feature.search.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface SearchActions {
    data class SearchQueryChanged(val query: String) : SearchActions
    data object Search : SearchActions
    data object Refresh : SearchActions
}

class SearchViewModel(
    private val dashboardInteractor: DashBoardInteractor,
) : BaseViewModel<SearchUiState>(SearchUiState()) {

    // Debouncing for search
    private var searchJob: Job? = null
    private val searchDebounceTime = 500L // 500ms debounce

    fun onAction(action: SearchActions) {
        when (action) {
            is SearchActions.SearchQueryChanged -> updateSearchQuery(action.query)
            SearchActions.Search -> searchFestivals()
            SearchActions.Refresh -> loadInitialData()
        }
    }

    private fun updateSearchQuery(query: String) {
        val currentState = getContent()
        setContent(currentState.copy(searchQuery = query))

        // Auto-search with debouncing
        searchJob?.cancel()
        searchJob = baseViewModelScope.launch {
            delay(searchDebounceTime)
            if (query.length >= 2) { // Only search if query is at least 2 characters
                searchFestivals()
            } else if (query.isEmpty()) {
                // Clear results if query is empty
                setContent(
                    currentState.copy(
                        searchQuery = query,
                        searchResults = emptyList(),
                        isSearching = false,
                    ),
                )
            }
        }
    }

    private fun searchFestivals() {
        val currentState = getContent()
        val query = currentState.searchQuery.trim()

        if (query.isBlank()) return

        setContent(currentState.copy(isSearching = true))
        baseViewModelScope.launch {
            // Use festival suggestions as search results for now
            // You can modify this to implement actual search logic
            val results = dashboardInteractor.getFestivalSuggestions()

            setContent(
                currentState.copy(
                    searchResults = results,
                    isSearching = false,
                ),
            )
        }
    }

    private fun loadInitialData() {
        setContent(SearchUiState())
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}
