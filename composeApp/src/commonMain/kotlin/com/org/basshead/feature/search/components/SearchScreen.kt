package com.org.basshead.feature.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.search_festivals
import basshead.composeapp.generated.resources.search_festivals_hint
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.FestivalItemState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<FestivalItemState>,
    isSearching: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onJoinFestival: (String) -> Unit = {},
    onViewLeaderboard: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.search_festivals),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(stringResource(Res.string.search_festivals_hint)) },
            modifier = Modifier.fillMaxWidth(),
        )

        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = searchResults,
                    key = { festival -> festival.id },
                ) { festival ->
                    FestivalItem(
                        festival = festival,
                        onJoinFestival = onJoinFestival,
                        onViewLeaderboard = onViewLeaderboard,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
            }
        }
    }
}
