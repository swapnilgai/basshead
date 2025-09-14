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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadBodySmall
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadHeadlineSmall
import com.org.basshead.design.atoms.BassheadTextField
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.atoms.BassheadTextButton
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.feature.search.presentation.SearchActions
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = BassheadTheme.spacing.medium, vertical = BassheadTheme.spacing.large),
    ) {
        // Header - Using atomic component
        BassheadHeadlineSmall(
            text = stringResource(Res.string.search_festivals),
            modifier = Modifier.padding(bottom = BassheadTheme.spacing.medium),
        )

        // Search bar with filter button - Optimized Row
        SearchBarSection(
            searchQuery = uiState.searchQuery,
            showFilters = uiState.showFilters,
            onAction = onAction,
        )

        // Filters panel
        AnimatedVisibility(
            visible = uiState.showFilters,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FilterPanel(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier.padding(vertical = BassheadTheme.spacing.small),
            )
        }

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))

        // Content based on state
        when {
            // Show loading when initially loading suggestions
            uiState.isLoadingMore && uiState.suggestionFestivals.isEmpty() && !uiState.hasSearched -> {
                LoadingSection()
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
                    listState = listState,
                )
            }

            uiState.shouldShowSuggestions -> {
                SuggestionsSection(
                    uiState = uiState,
                    onAction = onAction,
                    listState = listState,
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

/**
 * Optimized search bar component using atomic design
 */
@Composable
private fun SearchBarSection(
    searchQuery: String,
    showFilters: Boolean,
    onAction: (SearchActions) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadTextField(
            value = searchQuery,
            onValueChange = { onAction(SearchActions.OnSearchQueryChanged(it)) },
            label = { Text(stringResource(Res.string.search_festivals_hint)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp),
                    tint = BassheadTheme.colors.onSurfaceVariant,
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onAction(SearchActions.OnSearchCleared) }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.size(20.dp),
                            tint = BassheadTheme.colors.onSurfaceVariant,
                        )
                    }
                }
            },
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(BassheadTheme.spacing.small))

        // Filter button
        IconButton(
            onClick = { onAction(SearchActions.ToggleFilters) },
        ) {
            Icon(
                Icons.Default.FilterList,
                contentDescription = "Filters",
                tint = if (showFilters) BassheadTheme.colors.primary else BassheadTheme.colors.onSurface,
            )
        }
    }
}

/**
 * Loading section using atomic design
 */
@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = BassheadTheme.colors.primary,
        )
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
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surfaceContainer,
            contentColor = BassheadTheme.colors.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.large),
        ) {
            BassheadTitleMedium(
                text = stringResource(Res.string.filters),
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))

            // Status filters
            BassheadBodyMedium(
                text = stringResource(Res.string.status),
                color = BassheadTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
            ) {
                val statusOptions = listOf(
                    "upcoming" to "Upcoming",
                    "ongoing" to "Ongoing",
                )

                items(statusOptions) { (value, label) ->
                    val isSelected = uiState.selectedStatusFilters.contains(value)
                    FilterChip(
                        onClick = {
                            onAction(SearchActions.OnStatusFilterChanged(value, !isSelected))
                        },
                        label = {
                            BassheadBodySmall(
                                text = label,
                                color = if (isSelected) BassheadTheme.colors.onPrimary else BassheadTheme.colors.onSurface,
                            )
                        },
                        selected = isSelected,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BassheadTheme.colors.primary,
                            selectedLabelColor = BassheadTheme.colors.onPrimary,
                            containerColor = BassheadTheme.colors.surface,
                            labelColor = BassheadTheme.colors.onSurface,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) BassheadTheme.colors.primary else BassheadTheme.colors.outline,
                            selectedBorderColor = BassheadTheme.colors.primary,
                            borderWidth = if (isSelected) 2.dp else 1.dp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))

            // Location filter
            BassheadBodyMedium(
                text = stringResource(Res.string.location),
                color = BassheadTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

            BassheadTextField(
                value = uiState.locationFilter,
                onValueChange = { onAction(SearchActions.OnLocationFilterChanged(it)) },
                label = { Text(stringResource(Res.string.location_hint)) },
                supportingText = { Text(stringResource(Res.string.location_examples)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))

            // Apply button
            BassheadButton(
                onClick = { onAction(SearchActions.ApplyFilters) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.hasFiltersChanged,
            ) {
                BassheadBodyLarge(
                    text = stringResource(Res.string.apply),
                    color = BassheadTheme.colors.onPrimary,
                )
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
            BassheadTitleMedium(
                text = stringResource(Res.string.recent_searches),
            )

            if (recentSearches.isNotEmpty()) {
                BassheadTextButton(
                    onClick = { onAction(SearchActions.ClearSearchHistory) },
                ) {
                    BassheadBodyMedium(
                        text = stringResource(Res.string.clear_all),
                        color = BassheadTheme.colors.primary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

        LazyColumn {
            items(recentSearches) { query ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { onAction(SearchActions.OnRecentSearchClicked(query)) },
                    elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level0),
                    colors = CardDefaults.cardColors(
                        containerColor = BassheadTheme.colors.surfaceContainer,
                        contentColor = BassheadTheme.colors.onSurface,
                    ),
                ) {
                    BassheadBodyMedium(
                        text = query,
                        modifier = Modifier.padding(BassheadTheme.spacing.large),
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
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (uiState.isSearching && uiState.searchResults.isEmpty()) {
            LoadingSection()
        } else if (uiState.shouldShowEmptyState) {
            // Empty search results state
            EmptySearchResultsSection()
        } else {
            // Results list
            BassheadBodyMedium(
                text = stringResource(Res.string.festivals_found, uiState.searchResults.size),
                color = BassheadTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = BassheadTheme.spacing.small),
            )

            FestivalList(
                festivals = uiState.searchResults,
                canLoadMore = uiState.hasMoreResults,
                isLoadingMore = uiState.isLoadingMore,
                onLoadMore = { onAction(SearchActions.LoadMoreResults) },
                onFestivalClick = { onAction(SearchActions.OnFestivalClicked(it)) },
                onJoinFestival = { onAction(SearchActions.JoinFestival(it)) },
                onViewLeaderboard = { onAction(SearchActions.ViewLeaderboard(it)) },
                listState = listState,
            )
        }
    }
}

/**
 * Empty search results section using atomic components
 */
@Composable
private fun EmptySearchResultsSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(BassheadTheme.spacing.extraLarge),
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = BassheadTheme.colors.onSurfaceVariant.copy(alpha = 0.6f),
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))

            BassheadTitleMedium(
                text = stringResource(Res.string.no_festivals_found),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

            BassheadBodyMedium(
                text = stringResource(Res.string.no_festivals_found_description),
                textAlign = TextAlign.Center,
                color = BassheadTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.large),
            )
        }
    }
}

@Composable
private fun SuggestionsSection(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    FestivalList(
        festivals = uiState.suggestionFestivals,
        canLoadMore = uiState.hasMoreSuggestions,
        isLoadingMore = uiState.isLoadingMore,
        onLoadMore = { onAction(SearchActions.LoadMoreSuggestions) },
        onFestivalClick = { onAction(SearchActions.OnFestivalClicked(it)) },
        onJoinFestival = { onAction(SearchActions.JoinFestival(it)) },
        onViewLeaderboard = { onAction(SearchActions.ViewLeaderboard(it)) },
        listState = listState,
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
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
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
                lastVisibleItemIndex >= (totalItemsNumber - 3)
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
                onFestivalClick = { onFestivalClick(festival.id) },
                onJoinFestival = { onJoinFestival(festival.id) },
                onViewLeaderboard = { onViewLeaderboard(festival.id) },
                modifier = Modifier.padding(vertical = BassheadTheme.spacing.extraSmall),
            )
        }

        // Always show loading indicator when loading more
        if (isLoadingMore && festivals.isNotEmpty()) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = BassheadTheme.spacing.large),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = BassheadTheme.colors.primary,
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
            modifier = Modifier.padding(BassheadTheme.spacing.extraLarge),
        ) {
            // Icon or illustration
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = BassheadTheme.colors.onSurfaceVariant.copy(alpha = 0.6f),
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))

            BassheadHeadlineSmall(
                text = stringResource(Res.string.no_festivals_available_title),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

            BassheadBodyMedium(
                text = stringResource(Res.string.no_festivals_available_description),
                textAlign = TextAlign.Center,
                color = BassheadTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.large),
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))

            // Action button
            BassheadButton(
                onClick = { onAction(SearchActions.Refresh) },
            ) {
                BassheadBodyLarge(
                    text = stringResource(Res.string.refresh),
                    color = BassheadTheme.colors.onPrimary,
                )
            }
        }
    }
}
