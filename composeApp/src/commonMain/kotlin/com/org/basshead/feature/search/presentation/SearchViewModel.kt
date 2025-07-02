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
    data object LoadMore : SearchActions
}

class SearchViewModel(
    private val dashboardInteractor: DashBoardInteractor,
) : BaseViewModel<SearchUiState>(SearchUiState()) {

    private val pageSize = 3
    private var loadMoreJob: Job? = null

    init {
        loadInitialData()
    }

    fun onAction(action: SearchActions) {
        when (action) {
            is SearchActions.SearchQueryChanged -> updateSearchQuery(action.query)
            SearchActions.Search -> { /* Keep for future search implementation */ }
            SearchActions.Refresh -> refresh()
            SearchActions.LoadMore -> loadMoreWithDebounce()
        }
    }

    private fun updateSearchQuery(query: String) {
        val currentState = getContent()
        setContent(currentState.copy(searchQuery = query))
        // For now, we just update the query but don't search
        // Pagination will show suggestionFestivals regardless
    }

    private fun loadInitialData() {
        setLoading()
        baseViewModelScope.launch {
            val suggestionFestivals = dashboardInteractor.getFestivalSuggestions(
                limit = pageSize, 
                lastSeenId = null
            )

            setContent(
                SearchUiState(
                    suggestionFestivals = suggestionFestivals,
                    hasMoreSuggestions = suggestionFestivals.size >= pageSize,
                    lastSeenId = suggestionFestivals.lastOrNull()?.id,
                )
            )
        }
    }

    private fun loadMoreWithDebounce() {
        // Cancel previous job if still running
        loadMoreJob?.cancel()
        loadMoreJob = baseViewModelScope.launch {
            delay(300) // 300ms debounce
            loadMore()
        }
    }

    private fun loadMore() {
        val currentState = getContent()
        
        // Early return if no more data available or already loading
        if (!currentState.hasMoreSuggestions || currentState.isLoadingMore) return
        
        // Set loading state in UI
        setContent(currentState.copy(isLoadingMore = true))

        baseViewModelScope.launch {
            val newSuggestions = dashboardInteractor.getFestivalSuggestions(
                limit = pageSize,
                lastSeenId = currentState.lastSeenId
            )

            // Filter out duplicates
            val filteredNewSuggestions = newSuggestions.filter { newItem ->
                currentState.suggestionFestivals.none { it.id == newItem.id }
            }

            val updatedState = currentState.copy(
                suggestionFestivals = currentState.suggestionFestivals + filteredNewSuggestions,
                hasMoreSuggestions = newSuggestions.size >= pageSize,
                lastSeenId = filteredNewSuggestions.lastOrNull()?.id ?: currentState.lastSeenId,
                isLoadingMore = false,
            )
            setContent(updatedState)
        }
    }

    private fun refresh() {
        loadMoreJob?.cancel()
        loadInitialData()
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
    }
}
