package com.org.basshead.feature.profile.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.profile.model.ProfileUiState
import com.org.basshead.navigation.Route
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

sealed interface ProfileActions {
    data object Refresh : ProfileActions
    data object Logout : ProfileActions
    data object NavigateToAvatarSelection : ProfileActions
    data class OnFestivalClicked(val festivalId: String) : ProfileActions
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
            ProfileActions.NavigateToAvatarSelection -> navigateToAvatarSelection()
            is ProfileActions.OnFestivalClicked -> onFestivalClicked(action.festivalId)
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
            navigate(Route.Auth::class.simpleName!!)
        }
    }

    private fun onFestivalClicked(festivalId: String) {
        // Navigate to festival details
        navigate(
            destination = "${Route.FestivalDetails::class.simpleName}/$festivalId",
        )
    }

    private fun viewLeaderboard(festivalId: String) {
        navigate("${Route.FestivalLeaderBoard::class.simpleName}/$festivalId")
    }

    private fun navigateToAvatarSelection() {
        navigate(Route.AvatarSelection::class.simpleName!!)
    }
}
