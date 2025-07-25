package com.org.basshead.feature.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.apply
import basshead.composeapp.generated.resources.clear_all
import basshead.composeapp.generated.resources.festivals_found
import basshead.composeapp.generated.resources.filters
import basshead.composeapp.generated.resources.location
import basshead.composeapp.generated.resources.location_examples
import basshead.composeapp.generated.resources.location_hint
import basshead.composeapp.generated.resources.no_festivals_available_description
import basshead.composeapp.generated.resources.no_festivals_available_title
import basshead.composeapp.generated.resources.no_festivals_found
import basshead.composeapp.generated.resources.no_festivals_found_description
import basshead.composeapp.generated.resources.recent_searches
import basshead.composeapp.generated.resources.refresh
import basshead.composeapp.generated.resources.search_festivals
import basshead.composeapp.generated.resources.search_festivals_hint
import basshead.composeapp.generated.resources.status
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.feature.search.presentation.SearchActions
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        // Header
        Text(
            text = stringResource(Res.string.search_festivals),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        // Search bar with filter button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { onAction(SearchActions.OnSearchQueryChanged(it)) },
                label = { Text(stringResource(Res.string.search_festivals_hint)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onAction(SearchActions.OnSearchCleared) }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Filter button
            IconButton(
                onClick = { onAction(SearchActions.ToggleFilters) },
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filters",
                    tint = if (uiState.showFilters) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // Filters panel
        AnimatedVisibility(
            visible = uiState.showFilters,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FilterPanel(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on state
        when {
            // Show loading when initially loading suggestions
            uiState.isLoadingMore && uiState.suggestionFestivals.isEmpty() && !uiState.hasSearched -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.shouldShowRecentSearches -> {
                RecentSearchesSection(
                    recentSearches = uiState.recentSearches,
                    onAction = onAction,
                )
            }

            uiState.shouldShowSearchResults -> {
                SearchResultsSection(
                    uiState = uiState,
                    onAction = onAction,
                )
            }

            uiState.shouldShowSuggestions -> {
                SuggestionsSection(
                    uiState = uiState,
                    onAction = onAction,
                )
            }

            uiState.shouldShowSuggestionsEmptyState -> {
                EmptyFestivalsSection(
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun FilterPanel(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.filters),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status filters
            Text(
                text = stringResource(Res.string.status),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val statusOptions = listOf(
                    "upcoming" to "Upcoming",
                    "ongoing" to "Ongoing",
                    "completed" to "Completed",
                    "all" to "All",
                )

                items(statusOptions) { (value, label) ->
                    val isSelected = uiState.selectedStatusFilters.contains(value)
                    FilterChip(
                        onClick = {
                            onAction(SearchActions.OnStatusFilterChanged(value, !isSelected))
                        },
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        selected = isSelected,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            borderWidth = if (isSelected) 2.dp else 1.dp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location filter
            Text(
                text = stringResource(Res.string.location),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.locationFilter,
                onValueChange = { onAction(SearchActions.OnLocationFilterChanged(it)) },
                label = { Text(stringResource(Res.string.location_hint)) },
                supportingText = {
                    Text(
                        text = stringResource(Res.string.location_examples),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apply button
            Button(
                onClick = { onAction(SearchActions.ApplyFilters) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.hasFiltersChanged,
            ) {
                Text(stringResource(Res.string.apply))
            }
        }
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<String>,
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.recent_searches),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            if (recentSearches.isNotEmpty()) {
                TextButton(onClick = { onAction(SearchActions.ClearSearchHistory) }) {
                    Text(stringResource(Res.string.clear_all))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(recentSearches) { query ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { onAction(SearchActions.OnRecentSearchClicked(query)) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Text(
                        text = query,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsSection(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (uiState.isSearching && uiState.searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.shouldShowEmptyState) {
            // Empty search results state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(Res.string.no_festivals_found),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.no_festivals_found_description),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        } else {
            // Results list
            Text(
                text = stringResource(Res.string.festivals_found, uiState.searchResults.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            FestivalList(
                festivals = uiState.searchResults,
                canLoadMore = uiState.hasMoreResults, // Use hasMoreResults directly
                isLoadingMore = uiState.isLoadingMore,
                onLoadMore = { onAction(SearchActions.LoadMoreResults) },
                onFestivalClick = { onAction(SearchActions.OnFestivalClicked(it)) },
                onJoinFestival = { onAction(SearchActions.JoinFestival(it)) },
                onViewLeaderboard = { onAction(SearchActions.ViewLeaderboard(it)) },
            )
        }
    }
}

@Composable
private fun SuggestionsSection(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    FestivalList(
        festivals = uiState.suggestionFestivals,
        canLoadMore = uiState.hasMoreSuggestions, // Remove the isLoadingMore check here
        isLoadingMore = uiState.isLoadingMore,
        onLoadMore = { onAction(SearchActions.LoadMoreSuggestions) },
        onFestivalClick = { onAction(SearchActions.OnFestivalClicked(it)) },
        onJoinFestival = { onAction(SearchActions.JoinFestival(it)) },
        onViewLeaderboard = { onAction(SearchActions.ViewLeaderboard(it)) },
        modifier = modifier,
    )
}

@Composable
private fun FestivalList(
    festivals: List<FestivalItemState>,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onFestivalClick: (String) -> Unit,
    onJoinFestival: (String) -> Unit,
    onViewLeaderboard: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    // Trigger load more when approaching the end of the list
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // Trigger when we're close to the end (3 items before the end) and we can load more
            canLoadMore &&
                !isLoadingMore &&
                totalItemsNumber > 0 &&
                lastVisibleItemIndex >= (totalItemsNumber - 3) // Trigger 3 items before the end for better UX
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = festivals,
            key = { festival -> festival.id },
        ) { festival ->
            FestivalItem(
                festival = festival,
                onJoinFestival = { onJoinFestival(festival.id) },
                onViewLeaderboard = { onViewLeaderboard(festival.id) },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onFestivalClick(festival.id) },
            )
        }

        // Always show loading indicator when loading more, regardless of canLoadMore
        if (isLoadingMore && festivals.isNotEmpty()) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyFestivalsSection(
    onAction: (SearchActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp),
        ) {
            // Icon or illustration
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.no_festivals_available_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.no_festivals_available_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action button
            Button(
                onClick = { onAction(SearchActions.Refresh) },
            ) {
                Text(stringResource(Res.string.refresh))
            }
        }
    }
}
