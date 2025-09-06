package com.org.basshead.feature.profile.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.joined_festivals
import basshead.composeapp.generated.resources.logout
import basshead.composeapp.generated.resources.profile
import basshead.composeapp.generated.resources.total_headbangs
import basshead.composeapp.generated.resources.user_label
import basshead.composeapp.generated.resources.user_unknown
import basshead.composeapp.generated.resources.your_festivals
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.DailyHeadbangState
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.UserProfileState
import org.jetbrains.compose.resources.stringResource

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
    // Remember expensive calculations
    val festivalsCount = remember(userFestivals) {
        userFestivals.size
    }

    val userName = remember(profile) {
        profile?.name ?: "Unknown"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        item(key = "profile_header") {
            Text(
                text = stringResource(Res.string.profile),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Text(
                text = profile?.name?.let { name ->
                    stringResource(Res.string.user_label, name)
                } ?: stringResource(Res.string.user_unknown),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = stringResource(Res.string.total_headbangs, totalHeadbangs),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = stringResource(Res.string.joined_festivals, festivalsCount),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Avatar selection button - enhanced visibility
            Button(
                onClick = onNavigateToAvatar,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Change Avatar")
            }
        }

        item(key = "festivals_header") {
            Text(
                text = stringResource(Res.string.your_festivals),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        items(
            items = userFestivals,
            key = { festival -> festival.id },
        ) { festival ->
            FestivalItem(
                festival = festival.copy(userJoined = true),
                onFestivalClick = onFestivalClick,
                onViewLeaderboard = onViewLeaderboard,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        item(key = "logout_button") {
            Button(
                onClick = onLogout,
                modifier = Modifier.padding(top = 24.dp),
            ) {
                Text(stringResource(Res.string.logout))
            }
        }
    }
}
