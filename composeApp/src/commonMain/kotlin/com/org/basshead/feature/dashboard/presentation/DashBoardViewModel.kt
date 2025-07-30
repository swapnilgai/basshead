package com.org.basshead.feature.dashboard.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.DashBoardUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface DashBoardActions {
    data object LoadMore : DashBoardActions
    data object Refresh : DashBoardActions
    data class OnFestivalClicked(val festivalId: String) : DashBoardActions
    data class JoinFestival(val festivalId: String) : DashBoardActions
    data class ViewLeaderboard(val festivalId: String) : DashBoardActions

    // Device sync actions
    data object SyncDevice : DashBoardActions
    data object OpenSettings : DashBoardActions
}

class DashBoardViewModel(
    private val dashBoardInteractor: DashBoardInteractor,
) : BaseViewModel<DashBoardUiState>(DashBoardUiState()) {

    private var loadMoreJob: Job? = null

    init {
        loadInitialData()
    }

    fun onAction(action: DashBoardActions) {
        when (action) {
            DashBoardActions.LoadMore -> { /* Dashboard doesn't support load more */ }
            DashBoardActions.Refresh -> refresh()
            is DashBoardActions.OnFestivalClicked -> onFestivalClicked(action.festivalId)
            is DashBoardActions.JoinFestival -> joinFestival(action.festivalId)
            is DashBoardActions.ViewLeaderboard -> viewLeaderboard(action.festivalId)
            DashBoardActions.SyncDevice -> syncDevice()
            DashBoardActions.OpenSettings -> openSettings()
        }
    }

    private fun loadInitialData() {
        setLoading()
        baseViewModelScope.launch {
            val festivalSuggestionsAsync = async { dashBoardInteractor.getFestivalSuggestions(limit = 1, lastSeenId = null) } // Only get 1 for dashboard
            val userFestivalAsync = async { dashBoardInteractor.getUserFestivals(limit = 1) } // Only get 1 for dashboard
            val profileAsync = async { dashBoardInteractor.getUserProfile() }
            val dailyHeadbangsAsync = async { dashBoardInteractor.getDailyHeadbangs(limit = 7) } // Get last 7 days
            val totalHeadbangsAsync = async { dashBoardInteractor.getTotalHeadbangs() } // Get total headbangs

            val suggestionFestivals = festivalSuggestionsAsync.await()
            val joinedFestivals = userFestivalAsync.await()
            val profile = profileAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()
            val totalHeadbangs = totalHeadbangsAsync.await()

            setContent(
                DashBoardUiState(
                    joinedFestivals = joinedFestivals,
                    suggestionFestivals = suggestionFestivals,
                    profile = profile,
                    dailyHeadbangs = dailyHeadbangs,
                    totalHeadbangs = totalHeadbangs,
                    hasMoreSuggestions = false, // Dashboard doesn't need pagination
                    lastSeenId = null, // Not needed for dashboard
                    // Simulate device connection - in real app this would come from device manager
                    isDeviceConnected = true,
                    isSyncing = false,
                ),
            )
        }
    }

    private fun refresh() {
        loadMoreJob?.cancel()
        loadInitialData()
    }

    private fun onFestivalClicked(festivalId: String) {
        // Navigate to festival details
        navigate("FestivalDetails/$festivalId")
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

    private fun syncDevice() {
        val currentState = getContent()
        if (currentState == null || !currentState.isDeviceConnected || currentState.isSyncing) return

        setContent(currentState.copy(isSyncing = true))

        baseViewModelScope.launch {
            // Simulate device sync - replace with actual device sync logic
            delay(3000)

            // After sync, refresh the data
            loadInitialData()
        }
    }

    private fun openSettings() {
        navigate("settings")
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
    }
}
