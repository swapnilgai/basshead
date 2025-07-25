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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.dismiss
import basshead.composeapp.generated.resources.error_unknown
import basshead.composeapp.generated.resources.join
import basshead.composeapp.generated.resources.leaderboard
import basshead.composeapp.generated.resources.retry
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

    // Remember the date text calculation - format for single line
    val dateText = remember(festival.dateString, festival.startTime) {
        val dateStr = festival.dateString ?: festival.startTime
        // Format date to be more compact - take only the first part if it's too long
        when {
            dateStr.length > 30 -> {
                // If it's a long date string, try to extract just the date part
                val parts = dateStr.split(" ")
                if (parts.size >= 3) {
                    "${parts[0]} ${parts[1]} ${parts[2]}" // Month Day Year
                } else {
                    dateStr.take(30) + "..."
                }
            }
            else -> dateStr
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
    ) {
        Column {
            // Festival Image
            AsyncImage(
                model = festival.imageUrl,
                contentDescription = "Festival Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Festival Name
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = festival.name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Location
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = festival.location,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Date - single line with ellipsis if too long
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = dateText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (festival.userJoined) {
                    // User has joined - show leaderboard button
                    Button(
                        onClick = onLeaderboardClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(Res.string.leaderboard))
                    }
                } else {
                    // User hasn't joined - show join button
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(Res.string.join))
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
