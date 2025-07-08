package com.org.basshead.feature.profile.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.profile.model.ProfileUiState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

sealed interface ProfileActions {
    data object Refresh : ProfileActions
    data object Logout : ProfileActions
    data class ViewLeaderboard(val festivalId: String) : ProfileActions
}

class ProfileViewModel(
    private val dashboardInteractor: DashBoardInteractor,
) : BaseViewModel<ProfileUiState>(ProfileUiState()) {

    init {
        loadInitialData()
    }

    fun onAction(action: ProfileActions) {
        when (action) {
            ProfileActions.Refresh -> refresh()
            ProfileActions.Logout -> logout()
            is ProfileActions.ViewLeaderboard -> viewLeaderboard(action.festivalId)
        }
    }

    private fun loadInitialData() {
        performDataLoad()
    }

    private fun refresh() {
        performDataLoad()
    }

    private fun performDataLoad() {
        setLoading()
        baseViewModelScope.launch {
            val profileAsync = async { dashboardInteractor.getUserProfile() }
            val userFestivalsAsync = async { dashboardInteractor.getUserFestivals() }
            val dailyHeadbangsAsync = async { dashboardInteractor.getDailyHeadbangs() }
            val totalHeadbangsAsync = async { dashboardInteractor.getTotalHeadbangs() }

            val profile = profileAsync.await()
            val userFestivals = userFestivalsAsync.await()
            val dailyHeadbangs = dailyHeadbangsAsync.await()
            val totalHeadbangs = totalHeadbangsAsync.await()

            setContent(
                ProfileUiState(
                    profile = profile,
                    userFestivals = userFestivals,
                    dailyHeadbangs = dailyHeadbangs,
                    totalHeadbangs = totalHeadbangs,
                ),
            )
        }
    }

    private fun logout() {
        baseViewModelScope.launch {
            // Implement logout logic here if needed
            navigate("login")
        }
    }

    private fun viewLeaderboard(festivalId: String) {
        navigate("leaderboard/$festivalId")
    }
}
