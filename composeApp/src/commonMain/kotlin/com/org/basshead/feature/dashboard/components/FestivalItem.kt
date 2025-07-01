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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.error_unknown
import coil3.compose.AsyncImage
import com.org.basshead.feature.dashboard.model.FestivalSuggestionState
import org.jetbrains.compose.resources.stringResource

@Composable
fun FestivalItem(
    festival: FestivalSuggestionState,
    modifier: Modifier = Modifier,
) {
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
//                placeholder = painterResource(Res.drawable.dog)
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Festival Name
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = festival.name,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = festival.location,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = festival.dateString,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
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
                Text("Dismiss")
            }

            onRetry?.let { retry ->
                Button(onClick = retry) {
                    Text("Retry")
                }
            }
        }
    }
}
