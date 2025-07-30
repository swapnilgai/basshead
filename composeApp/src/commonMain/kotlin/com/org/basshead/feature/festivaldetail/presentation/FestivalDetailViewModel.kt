package com.org.basshead.feature.festivaldetail.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.toFestivalItemState
import com.org.basshead.feature.festivaldetail.interactor.FestivalCacheInteractor
import com.org.basshead.feature.festivaldetail.model.FestivalDetailUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

sealed interface FestivalDetailActions {
    data object Refresh : FestivalDetailActions
    data object JoinFestival : FestivalDetailActions
    data object ViewLeaderboard : FestivalDetailActions
    data object NavigateBack : FestivalDetailActions
}

class FestivalDetailViewModel(
    private val dashboardInteractor: DashBoardInteractor,
    private val festivalCacheInteractor: FestivalCacheInteractor,
    private val festivalId: String,
) : BaseViewModel<FestivalDetailUiState>(FestivalDetailUiState()) {

    init {
        loadFestivalDetails()
    }

    fun onAction(action: FestivalDetailActions) {
        when (action) {
            FestivalDetailActions.Refresh -> refresh()
            FestivalDetailActions.JoinFestival -> joinFestival()
            FestivalDetailActions.ViewLeaderboard -> viewLeaderboard()
            FestivalDetailActions.NavigateBack -> navigateBack()
        }
    }

    private fun loadFestivalDetails() {
        val currentState = getContent()
        setContent(currentState.copy(isRefreshing = true, joinError = null))
        
        baseViewModelScope.launch {
            // Use parallel async calls for better performance
            val userFestivalsDeferred = async { dashboardInteractor.getUserFestivals() }
            val cachedFestivalDeferred = async { festivalCacheInteractor.getFestivalById(festivalId) }
            
            // Await both parallel calls
            val userFestivals = userFestivalsDeferred.await()
            val cachedFestival = cachedFestivalDeferred.await()
            
            // Check if festival is in user festivals (they've joined it)
            val userFestival = userFestivals.find { it.id == festivalId }
            
            // Use cached festival data or user festival data (cached is preferred for most up-to-date info)
            val festivalToShow = cachedFestival ?: userFestival
            
            if (festivalToShow != null) {
                setContent(
                    FestivalDetailUiState(
                        festival = festivalToShow,
                        isJoining = false,
                        joinError = null,
                        isRefreshing = false,
                        userInteractionEnabled = true,
                    )
                )
                return@launch
            }
            
            // If not found in cache, get from suggestions as fallback
        }
    }

    private fun refresh() {
        loadFestivalDetails()
    }

    private fun joinFestival() {
        val currentState = getContent()
        val festival = currentState.festival ?: return
        
        if (festival.userJoined) {
            // Already joined, navigate to leaderboard
            viewLeaderboard()
            return
        }

        setContent(
            currentState.copy(
                isJoining = true, 
                joinError = null,
                userInteractionEnabled = false
            )
        )
        
        baseViewModelScope.launch {
            // TODO: Implement actual join festival logic in interactor
            // For now, we'll just simulate success with a delay
            kotlinx.coroutines.delay(1500) // Simulate network call
            // dashboardInteractor.joinFestival(festivalId)
            
            // Refresh data to get updated state
            loadFestivalDetails()
        }
    }

    private fun viewLeaderboard() {
        navigate("leaderboard/$festivalId")
    }

    private fun navigateBack() {
        navigate(
            destination = "Dashboard",
        )
    }
}
