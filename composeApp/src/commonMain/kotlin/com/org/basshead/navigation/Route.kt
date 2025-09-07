package com.org.basshead.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash

    @Serializable
    data object Auth

    @Serializable
    data object Dashboard

    @Serializable
    data object Profile

    @Serializable
    data object Search

    @Serializable
    data object AvatarSelection

    @Serializable
    data class FestivalDetails(val festivalID: String)

    @Serializable
    data class FestivalLeaderBoard(val festivalID: String)
}
