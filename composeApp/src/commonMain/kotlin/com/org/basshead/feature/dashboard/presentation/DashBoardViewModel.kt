package com.org.basshead.feature.dashboard.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.FestivalSuggestionState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashBoardActions {
    object LoadMore : DashBoardActions
    object Refresh : DashBoardActions
}

class DashBoardViewModel(
    private val dashBoardInteractor: DashBoardInteractor
) : BaseViewModel<List<FestivalSuggestionState>>(emptyList()) {

    // Pagination state
    private var lastSeenId: String? = null
    private var isLoadingMoreInternal = false
    private val pageSize = 3

    // Exposed states for UI
    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    // Debouncing
    private var loadMoreJob: Job? = null

    init {
        loadInitialData()
    }

    fun onAction(action: DashBoardActions) {
        when (action) {
            DashBoardActions.LoadMore -> loadMoreWithDebounce()
            DashBoardActions.Refresh -> refresh()
        }
    }

    private fun loadInitialData() {
        resetPaginationState()
        setLoading()
        baseViewModelScope.launch {
                val result = dashBoardInteractor.getFestivalSuggestions(
                    limit = pageSize,
                    lastSeenId = null
                )
                updatePaginationState(result)
                setContent(result)
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
        // Early return conditions to prevent unnecessary calls
        if (isLoadingMoreInternal || !_hasMore.value) return

        val currentList = getContent()
        if (currentList.isEmpty()) return

        isLoadingMoreInternal = true
        _isLoadingMore.value = true

        baseViewModelScope.launch {
                val result = dashBoardInteractor.getFestivalSuggestions(
                    limit = pageSize,
                    lastSeenId = lastSeenId
                )

                val currentList = getContent()

                // Only add truly new items (double-check for duplicates)
                val newItems = result.filter { newItem ->
                    currentList.none { it.id == newItem.id }
                }

                if (newItems.isNotEmpty()) {
                    val updatedList = currentList + newItems
                    setContent(updatedList)

                    // Update pagination state - use the last item from NEW items, not the combined list
                    lastSeenId = newItems.lastOrNull()?.id
                    _hasMore.value = result.size == pageSize
                } else {
                    // No new items means we've reached the end
                    _hasMore.value = false
                }
                isLoadingMoreInternal = false
                _isLoadingMore.value = false

        }
    }

    fun refresh() {
        resetPaginationState()
        loadInitialData()
    }

    private fun resetPaginationState() {
        lastSeenId = null
        isLoadingMoreInternal = false
        _hasMore.value = true
        _isLoadingMore.value = false
        loadMoreJob?.cancel()
    }

    private fun updatePaginationState(result: List<FestivalSuggestionState>) {
        lastSeenId = result.lastOrNull()?.id
        _hasMore.value = result.size == pageSize
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
    }
}