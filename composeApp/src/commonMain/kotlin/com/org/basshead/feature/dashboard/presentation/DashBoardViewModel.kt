package com.org.basshead.feature.dashboard.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.DashBoardUiState
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.utils.core.UiText
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashBoardActions {
    object LoadMore : DashBoardActions
    object Refresh : DashBoardActions
    data class JoinFestival(val festivalId: String) : DashBoardActions
    data class ViewLeaderboard(val festivalId: String) : DashBoardActions
}

class DashBoardViewModel(
    private val dashBoardInteractor: DashBoardInteractor,
) : BaseViewModel<DashBoardUiState>(DashBoardUiState()) {

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
            is DashBoardActions.JoinFestival -> joinFestival(action.festivalId)
            is DashBoardActions.ViewLeaderboard -> viewLeaderboard(action.festivalId)
        }
    }

    private fun loadInitialData() {
        setLoading()
        resetPaginationState()
        baseViewModelScope.launch {
            val festivalSuggestionsAsync = async { dashBoardInteractor.getFestivalSuggestions(limit = pageSize, lastSeenId = null) }
            val userFestivalAsync = async { dashBoardInteractor.getUserFestivals() }
            val profileAsync = async { dashBoardInteractor.getUserProfile() }
            val dailyHeadbangsAsync = async { dashBoardInteractor.getDailyHeadbangs(limit = 7) } // Get last 7 days

            val suggestionFestivals = festivalSuggestionsAsync.await()
            val joinedFestivals = userFestivalAsync.await()
            val profile = profileAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()

            updatePaginationState(suggestionFestivals)
            setContent(
                DashBoardUiState(
                    joinedFestivals = joinedFestivals,
                    suggestionFestivals = suggestionFestivals,
                    profile = profile,
                    dailyHeadbangs = dailyHeadbangs,
                ),
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
        // Early return conditions to prevent unnecessary calls
        if (isLoadingMoreInternal || !_hasMore.value) return

        val currentState = getContent()
        val currentSuggestionList = currentState.suggestionFestivals
        val currentJoinedList = currentState.joinedFestivals
        if (currentSuggestionList.isEmpty() && currentJoinedList.isEmpty()) return

        setLoading()
        isLoadingMoreInternal = true
        _isLoadingMore.value = true

        baseViewModelScope.launch {
            // Only fetch festival suggestions for pagination - user festivals don't change as frequently
            val festivalSuggestionsAsync = async { dashBoardInteractor.getFestivalSuggestions(limit = pageSize, lastSeenId = lastSeenId) }
            val userFestivalAsync = async { dashBoardInteractor.getUserFestivals() }
            val profileAsync = async { dashBoardInteractor.getUserProfile() }
            val dailyHeadbangsAsync = async { dashBoardInteractor.getDailyHeadbangs(limit = 7) }

            val suggestionFestivals = festivalSuggestionsAsync.await()
            val joinedFestivals = userFestivalAsync.await()
            val profile = profileAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()

            // Only add truly new items (double-check for duplicates)
            val newSuggestions = suggestionFestivals.filter { newItem ->
                currentSuggestionList.none { it.id == newItem.id }
            }
            val newJoined = joinedFestivals.filter { newItem ->
                currentJoinedList.none { it.id == newItem.id }
            }

            if (newSuggestions.isNotEmpty() || newJoined.isNotEmpty()) {
                val updatedState = DashBoardUiState(
                    joinedFestivals = currentJoinedList + newJoined,
                    suggestionFestivals = currentSuggestionList + newSuggestions,
                    profile = profile,
                    dailyHeadbangs = dailyHeadbangs,
                )
                setContent(updatedState)

                // Update pagination state - use the last item from NEW suggestions
                lastSeenId = newSuggestions.lastOrNull()?.id
                _hasMore.value = suggestionFestivals.size == pageSize
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

    private fun updatePaginationState(result: List<FestivalItemState>) {
        lastSeenId = result.lastOrNull()?.id
        _hasMore.value = result.size == pageSize
    }

    private fun joinFestival(festivalId: String) {
        baseViewModelScope.launch {
            try {
                // TODO: Implement festival joining logic via interactor
                // dashBoardInteractor.joinFestival(festivalId)

                // Refresh the data to update the UI
                refresh()
            } catch (e: Exception) {
                setError(UiText.DynamicString(e.message ?: "Failed to join festival"))
            }
        }
    }

    private fun viewLeaderboard(festivalId: String) {
        navigate("leaderboard/$festivalId")
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
    }
}
