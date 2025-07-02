package com.org.basshead.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.dismiss
import basshead.composeapp.generated.resources.error_unknown
import basshead.composeapp.generated.resources.join_for_fun
import basshead.composeapp.generated.resources.retry
import basshead.composeapp.generated.resources.view_leaderboard
import coil3.compose.AsyncImage
import com.org.basshead.feature.dashboard.model.FestivalItemState
import org.jetbrains.compose.resources.stringResource

@Composable
fun FestivalItem(
    festival: FestivalItemState,
    onJoinFestival: (String) -> Unit = {},
    onViewLeaderboard: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // Remember callback functions to avoid recomposition
    val onJoinClick = remember(festival.id) {
        {
            onJoinFestival(festival.id)
        }
    }

    val onLeaderboardClick = remember(festival.id) {
        {
            onViewLeaderboard(festival.id)
        }
    }

    // Remember the date text calculation
    val dateText = remember(festival.dateString, festival.startTime) {
        festival.dateString ?: festival.startTime
    }

    // Remember button arrangement calculation
    val buttonArrangement = remember(festival.userJoined) {
        if (festival.userJoined) Arrangement.Center else Arrangement.spacedBy(8.dp)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        elevation = 4.dp,
    ) {
        Column {
            // Festival Image
            AsyncImage(
                model = festival.imageUrl,
                contentDescription = "Festival Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Festival Name
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = festival.name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Location
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = festival.location,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Date
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = dateText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = buttonArrangement,
            ) {
                if (festival.userJoined) {
                    // Show only "View Leaderboard" for joined festivals
                    Button(
                        onClick = onLeaderboardClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(Res.string.view_leaderboard))
                    }
                } else {
                    // Show both buttons for suggestion festivals
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(Res.string.join_for_fun))
                    }

                    Button(
                        onClick = onLeaderboardClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(Res.string.view_leaderboard))
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(
    errorMessage: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.error_unknown),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.error,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = onDismiss) {
                Text(stringResource(Res.string.dismiss))
            }

            onRetry?.let { retry ->
                Button(onClick = retry) {
                    Text(stringResource(Res.string.retry))
                }
            }
        }
    }
}
