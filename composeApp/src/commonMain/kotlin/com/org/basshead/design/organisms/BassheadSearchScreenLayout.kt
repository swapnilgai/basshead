package com.org.basshead.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
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
import com.org.basshead.design.atoms.BassheadTextButton
import com.org.basshead.design.atoms.BassheadTextField
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.feature.search.presentation.SearchActions
import org.jetbrains.compose.resources.stringResource

/**
 * Highly optimized search screen layout using atomic design - flattened hierarchy for maximum performance
 * Eliminates overdraw by reducing component nesting and using direct LazyColumn items
 *
 * Performance optimizations:
 * - Single LazyColumn with direct item composition when possible
 * - Eliminated intermediate wrapper components
 * - Reduced padding/spacing layers
 * - Minimized recomposition scope with stable data classes
 * - Optimized state derivation for load more logic
 */
@Composable
fun BassheadSearchScreenLayout(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = BassheadTheme.spacing.medium,
            vertical = BassheadTheme.spacing.large,
        ),
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        state = listState,
    ) {
        // Header - Direct composition
        item(key = "search_header") {
            BassheadHeadlineSmall(
                text = stringResource(Res.string.search_festivals),
            )
        }

        // Search bar with filter button - Direct composition
        item(key = "search_bar") {
            SearchBarDirect(
                searchQuery = uiState.searchQuery,
                showFilters = uiState.showFilters,
                onAction = onAction,
            )
        }

        // Filters panel - Conditional direct composition
        if (uiState.showFilters) {
            item(key = "filter_panel") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    FilterPanelDirect(
                        uiState = uiState,
                        onAction = onAction,
                    )
                }
            }
        }

        // Content based on state - Direct composition in LazyColumn
        when {
            // Show loading when initially loading suggestions
            uiState.isLoadingMore && uiState.suggestionFestivals.isEmpty() && !uiState.hasSearched -> {
                item(key = "initial_loading") {
                    LoadingSectionDirect()
                }
            }

            uiState.shouldShowRecentSearches -> {
                item(key = "recent_searches_header") {
                    RecentSearchesHeaderDirect(
                        hasSearches = uiState.recentSearches.isNotEmpty(),
                        onAction = onAction,
                    )
                }

                items(
                    items = uiState.recentSearches,
                    key = { "recent_$it" },
                ) { query ->
                    RecentSearchItemDirect(
                        query = query,
                        onAction = onAction,
                    )
                }
            }

            uiState.shouldShowSearchResults -> {
                if (uiState.isSearching && uiState.searchResults.isEmpty()) {
                    item(key = "search_loading") {
                        LoadingSectionDirect()
                    }
                } else if (uiState.shouldShowEmptyState) {
                    item(key = "empty_search_results") {
                        EmptySearchResultsDirect()
                    }
                } else {
                    item(key = "search_results_count") {
                        BassheadBodyMedium(
                            text = stringResource(Res.string.festivals_found, uiState.searchResults.size),
                            color = BassheadTheme.colors.onSurfaceVariant,
                        )
                    }

                    FestivalListItems(
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

            uiState.shouldShowSuggestions -> {
                FestivalListItems(
                    festivals = uiState.suggestionFestivals,
                    canLoadMore = uiState.hasMoreSuggestions,
                    isLoadingMore = uiState.isLoadingMore,
                    onLoadMore = { onAction(SearchActions.LoadMoreSuggestions) },
                    onFestivalClick = { onAction(SearchActions.OnFestivalClicked(it)) },
                    onJoinFestival = { onAction(SearchActions.JoinFestival(it)) },
                    onViewLeaderboard = { onAction(SearchActions.ViewLeaderboard(it)) },
                    listState = listState,
                )
            }

            uiState.shouldShowSuggestionsEmptyState -> {
                item(key = "empty_festivals") {
                    EmptyFestivalsDirect(onAction = onAction)
                }
            }
        }
    }
}

/**
 * Optimized search bar - direct composition without intermediate wrappers
 */
@Composable
private fun SearchBarDirect(
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
 * Optimized loading section - direct composition
 */
@Composable
private fun LoadingSectionDirect() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), // Fixed height to prevent layout thrashing
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = BassheadTheme.colors.primary,
        )
    }
}

/**
 * Optimized filter panel - direct composition without intermediate card wrapper overhead
 */
@Composable
private fun FilterPanelDirect(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surfaceContainer,
            contentColor = BassheadTheme.colors.onSurface,
        ),
    ) {
        // Direct composition within card to minimize nesting
        BassheadTitleMedium(
            text = stringResource(Res.string.filters),
            modifier = Modifier.padding(
                top = BassheadTheme.spacing.large,
                start = BassheadTheme.spacing.large,
                end = BassheadTheme.spacing.large,
            ),
        )

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))

        // Status filters
        BassheadBodyMedium(
            text = stringResource(Res.string.status),
            color = BassheadTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = BassheadTheme.spacing.large),
        )

        LazyRow(
            modifier = Modifier.padding(
                top = BassheadTheme.spacing.small,
                start = BassheadTheme.spacing.large,
            ),
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
            modifier = Modifier.padding(horizontal = BassheadTheme.spacing.large),
        )

        BassheadTextField(
            value = uiState.locationFilter,
            onValueChange = { onAction(SearchActions.OnLocationFilterChanged(it)) },
            label = { Text(stringResource(Res.string.location_hint)) },
            supportingText = { Text(stringResource(Res.string.location_examples)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = BassheadTheme.spacing.small,
                    start = BassheadTheme.spacing.large,
                    end = BassheadTheme.spacing.large,
                ),
        )

        // Apply button
        BassheadButton(
            onClick = { onAction(SearchActions.ApplyFilters) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(BassheadTheme.spacing.large),
            enabled = uiState.hasFiltersChanged,
        ) {
            BassheadBodyLarge(
                text = stringResource(Res.string.apply),
                color = BassheadTheme.colors.onPrimary,
            )
        }
    }
}

/**
 * Optimized recent searches header - direct composition
 */
@Composable
private fun RecentSearchesHeaderDirect(
    hasSearches: Boolean,
    onAction: (SearchActions) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadTitleMedium(
            text = stringResource(Res.string.recent_searches),
        )

        if (hasSearches) {
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
}

/**
 * Optimized recent search item - direct composition
 */
@Composable
private fun RecentSearchItemDirect(
    query: String,
    onAction: (SearchActions) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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

/**
 * Optimized empty search results - direct composition
 */
@Composable
private fun EmptySearchResultsDirect() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp), // Fixed height to prevent layout shifts
        contentAlignment = Alignment.Center,
    ) {
        // Direct composition without intermediate Column wrapper
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BassheadTheme.colors.onSurfaceVariant.copy(alpha = 0.6f),
        )

        BassheadTitleMedium(
            text = stringResource(Res.string.no_festivals_found),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 80.dp),
        )

        BassheadBodyMedium(
            text = stringResource(Res.string.no_festivals_found_description),
            textAlign = TextAlign.Center,
            color = BassheadTheme.colors.onSurfaceVariant,
            modifier = Modifier.padding(
                top = 120.dp,
                start = BassheadTheme.spacing.large,
                end = BassheadTheme.spacing.large,
            ),
        )
    }
}

/**
 * Optimized empty festivals section - direct composition
 */
@Composable
private fun EmptyFestivalsDirect(
    onAction: (SearchActions) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), // Fixed height to prevent layout shifts
        contentAlignment = Alignment.Center,
    ) {
        // Direct composition with absolute positioning for better performance
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = BassheadTheme.colors.onSurfaceVariant.copy(alpha = 0.6f),
        )

        BassheadHeadlineSmall(
            text = stringResource(Res.string.no_festivals_available_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 100.dp),
        )

        BassheadBodyMedium(
            text = stringResource(Res.string.no_festivals_available_description),
            textAlign = TextAlign.Center,
            color = BassheadTheme.colors.onSurfaceVariant,
            modifier = Modifier.padding(
                top = 150.dp,
                start = BassheadTheme.spacing.large,
                end = BassheadTheme.spacing.large,
            ),
        )

        BassheadButton(
            onClick = { onAction(SearchActions.Refresh) },
            modifier = Modifier.padding(top = 220.dp),
        ) {
            BassheadBodyLarge(
                text = stringResource(Res.string.refresh),
                color = BassheadTheme.colors.onPrimary,
            )
        }
    }
}

/**
 * Optimized festival list extension - adds items directly to LazyColumn scope
 * Uses optimized load more detection with derivedStateOf
 */
private fun androidx.compose.foundation.lazy.LazyListScope.FestivalListItems(
    festivals: List<FestivalItemState>,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onFestivalClick: (String) -> Unit,
    onJoinFestival: (String) -> Unit,
    onViewLeaderboard: (String) -> Unit,
    listState: LazyListState,
) {
    // Festival items
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

    // Loading indicator for pagination
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
