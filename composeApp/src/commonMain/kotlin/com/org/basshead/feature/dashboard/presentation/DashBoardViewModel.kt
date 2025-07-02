package com.org.basshead.feature.dashboard.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.DashBoardUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

    private val pageSize = 3
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
        baseViewModelScope.launch {
            val festivalSuggestionsAsync = async { dashBoardInteractor.getFestivalSuggestions(limit = pageSize, lastSeenId = null) }
            val userFestivalAsync = async { dashBoardInteractor.getUserFestivals() }
            val profileAsync = async { dashBoardInteractor.getUserProfile() }
            val dailyHeadbangsAsync = async { dashBoardInteractor.getDailyHeadbangs(limit = 7) } // Get last 7 days

            val suggestionFestivals = festivalSuggestionsAsync.await()
            val joinedFestivals = userFestivalAsync.await()
            val profile = profileAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()

            setContent(
                DashBoardUiState(
                    joinedFestivals = joinedFestivals,
                    suggestionFestivals = suggestionFestivals,
                    profile = profile,
                    dailyHeadbangs = dailyHeadbangs,
                    hasMoreSuggestions = suggestionFestivals.size >= pageSize,
                    lastSeenId = suggestionFestivals.lastOrNull()?.id,
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
        val currentState = getContent()
        
        // Early return conditions to prevent unnecessary calls
        if (!currentState.hasMoreSuggestions) return

        // Don't set loading here since we want to show loading indicator while keeping content visible
        baseViewModelScope.launch {
            // Only fetch festival suggestions for pagination - user festivals don't change as frequently
            val festivalSuggestionsAsync = async { 
                dashBoardInteractor.getFestivalSuggestions(limit = pageSize, lastSeenId = currentState.lastSeenId) 
            }
            val userFestivalAsync = async { dashBoardInteractor.getUserFestivals() }
            val profileAsync = async { dashBoardInteractor.getUserProfile() }
            val dailyHeadbangsAsync = async { dashBoardInteractor.getDailyHeadbangs(limit = 7) }

            val suggestionFestivals = festivalSuggestionsAsync.await()
            val joinedFestivals = userFestivalAsync.await()
            val profile = profileAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()

            // Only add truly new items (double-check for duplicates)
            val newSuggestions = suggestionFestivals.filter { newItem ->
                currentState.suggestionFestivals.none { it.id == newItem.id }
            }
            val newJoined = joinedFestivals.filter { newItem ->
                currentState.joinedFestivals.none { it.id == newItem.id }
            }

            val updatedState = DashBoardUiState(
                joinedFestivals = currentState.joinedFestivals + newJoined,
                suggestionFestivals = currentState.suggestionFestivals + newSuggestions,
                profile = profile,
                dailyHeadbangs = dailyHeadbangs,
                hasMoreSuggestions = suggestionFestivals.size >= pageSize,
                lastSeenId = newSuggestions.lastOrNull()?.id ?: currentState.lastSeenId,
            )
            setContent(updatedState)
        }
    }

    fun refresh() {
        loadMoreJob?.cancel()
        loadInitialData()
    }

    private fun joinFestival(festivalId: String) {
        baseViewModelScope.launch {
            // TODO: Implement festival joining logic via interactor
            // dashBoardInteractor.joinFestival(festivalId)

            // Refresh the data to update the UI
            refresh()
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
