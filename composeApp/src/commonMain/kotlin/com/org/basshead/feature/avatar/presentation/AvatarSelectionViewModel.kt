package com.org.basshead.feature.avatar.presentation

import com.org.basshead.feature.avatar.interactor.AvatarInteractor
import com.org.basshead.feature.avatar.model.AvatarSelectionUiState
import com.org.basshead.navigation.Route
import com.org.basshead.utils.core.UiText
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.launch

sealed interface AvatarSelectionActions {
    data object LoadAvatars : AvatarSelectionActions
    data object SaveAvatar : AvatarSelectionActions
    data class SelectAvatar(val avatarUrl: String) : AvatarSelectionActions
    data object NavigateBack : AvatarSelectionActions
}

class AvatarSelectionViewModel(
    private val avatarInteractor: AvatarInteractor
) : BaseViewModel<AvatarSelectionUiState>(AvatarSelectionUiState()) {

    init {
        loadAvatars()
    }

    fun onAction(action: AvatarSelectionActions) {
        when (action) {
            is AvatarSelectionActions.LoadAvatars -> loadAvatars()
            is AvatarSelectionActions.SaveAvatar -> saveAvatar()
            is AvatarSelectionActions.SelectAvatar -> selectAvatar(action.avatarUrl)
            is AvatarSelectionActions.NavigateBack -> navigateBack()
        }
    }

    private fun loadAvatars() {
        baseViewModelScope.launch {
            setLoading()

            // Get current user avatar
            val currentAvatarUrl = avatarInteractor.getCurrentUserAvatarUrl()!!

            // Get all available avatars
            val avatars = avatarInteractor.getAvatars()

            val updatedState = getContent().copy(
                avatars = avatars,
                currentUserAvatarUrl = currentAvatarUrl,
                selectedAvatarUrl = currentAvatarUrl // Initialize selection with current user avatar
            )
            setContent(updatedState)
        }
    }

    private fun saveAvatar() {
        baseViewModelScope.launch {
            val updatedState = getContent().copy(isSaving = true)
            setContent(updatedState)

            // Get the currently selected avatar URL from the state
            val avatarUrl = getContent().selectedAvatarUrl

            avatarInteractor.updateUserAvatar(avatarUrl)

            // Navigate back to previous screen
            navigateBack()
        }
    }

    private fun selectAvatar(avatarUrl: String) {
        // Update the selected avatar URL in the state
        val updatedState = getContent().copy(selectedAvatarUrl = avatarUrl)
        setContent(updatedState)
    }
}
