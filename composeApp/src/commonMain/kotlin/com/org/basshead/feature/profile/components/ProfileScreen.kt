package com.org.basshead.feature.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.org.basshead.design.organisms.BassheadProfileScreenLayout
import com.org.basshead.feature.dashboard.model.DailyHeadbangState
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.UserProfileState

@Stable
data class ProfileDisplayData(
    val userName: String,
    val totalHeadbangs: String,
    val festivalsCount: String,
)

@Composable
fun ProfileScreen(
    profile: UserProfileState?,
    userFestivals: List<FestivalItemState>,
    dailyHeadbangs: List<DailyHeadbangState>,
    totalHeadbangs: Long,
    onLogout: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    onFestivalClick: (String) -> Unit = {},
    onViewLeaderboard: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // Remember expensive calculations and stable data
    val profileDisplayData = remember(profile, totalHeadbangs, userFestivals) {
        ProfileDisplayData(
            userName = profile?.name ?: "Unknown User",
            totalHeadbangs = totalHeadbangs.toString(),
            festivalsCount = userFestivals.size.toString(),
        )
    }

    BassheadProfileScreenLayout(
        userName = profileDisplayData.userName,
        totalHeadbangs = profileDisplayData.totalHeadbangs,
        festivalsCount = profileDisplayData.festivalsCount,
        onLogout = onLogout,
        onNavigateToAvatar = onNavigateToAvatar,
        modifier = modifier,
    )
}
