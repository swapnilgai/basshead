package com.org.basshead.feature.avatar.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Avatar(
    val id: Long,
    val url: String
)

@Immutable
data class AvatarSelectionUiState(
    val avatars: List<Avatar> = emptyList(),
    val currentUserAvatarUrl: String = "",
    val selectedAvatarUrl: String = "",
    val isSaving: Boolean = false
)
