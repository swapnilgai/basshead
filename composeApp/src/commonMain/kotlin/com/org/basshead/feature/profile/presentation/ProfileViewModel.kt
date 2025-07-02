package com.org.basshead.feature.profile.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.profile.model.ProfileUiState
import com.org.basshead.utils.core.UiText
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
            try {
                val profileAsync = async { dashboardInteractor.getUserProfile() }
                val userFestivalsAsync = async { dashboardInteractor.getUserFestivals() }
                val dailyHeadbangsAsync = async { dashboardInteractor.getDailyHeadbangs() }

                val profile = profileAsync.await()
                val userFestivals = userFestivalsAsync.await()
                val dailyHeadbangs = dailyHeadbangsAsync.await()

                setContent(
                    ProfileUiState(
                        profile = profile,
                        userFestivals = userFestivals,
                        dailyHeadbangs = dailyHeadbangs,
                    ),
                )
            } catch (e: Exception) {
                setError(UiText.DynamicString(e.message ?: "Failed to load profile"))
            }
        }
    }

    private fun logout() {
        baseViewModelScope.launch {
            try {
                // Implement logout logic here if needed
                navigate("login")
            } catch (e: Exception) {
                setError(UiText.DynamicString(e.message ?: "Logout failed"))
            }
        }
    }

    private fun viewLeaderboard(festivalId: String) {
        navigate("leaderboard/$festivalId")
    }
}
