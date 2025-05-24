package com.org.basshead.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Dashboard

    @Serializable
    data object Profile

    @Serializable
    data class FestivalDetails(val festivalID: String)

    @Serializable
    data class FestivalLeaderBoard(val festivalID: String)

}