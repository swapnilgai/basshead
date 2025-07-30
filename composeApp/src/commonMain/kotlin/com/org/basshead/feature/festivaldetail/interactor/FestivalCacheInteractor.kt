package com.org.basshead.feature.festivaldetail.interactor

import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.search.interactor.SaveSearchCacheResult
import com.org.basshead.feature.search.interactor.SearchInteractor
import com.org.basshead.utils.interactor.Interactor

/**
 * Interactor for retrieving festivals from cached search results
 */
interface FestivalCacheInteractor : Interactor {
    /**
     * Find a festival by ID from cached search results
     */
    suspend fun getFestivalById(festivalId: String): FestivalItemState?
}

class FestivalCacheInteractorImpl(
    private val searchInteractor: SearchInteractor,
) : FestivalCacheInteractor {

    override suspend fun getFestivalById(festivalId: String): FestivalItemState? {
        // Get recent searches from SearchInteractor's existing cache
        val recentSearches: List<SaveSearchCacheResult> = searchInteractor.getRecentSearchesCacheKey()

        // Iterate through recent searches and try to find the festival
        for (searchCache in recentSearches) {
            // Try to get cached search results for this search cache
            val searchResults = searchInteractor.searchFestivals(
                query = searchCache.query,
                statusFilters = searchCache.statusFilters ?: emptyList(),
                locationFilter = searchCache.locationFilter,
                limit = searchCache.limit,
                lastSeenId = searchCache.lastSeenId
            )

            // Look for the festival with matching ID
            val festival = searchResults.find { it.id == festivalId }
            if (festival != null) {
                return festival
            }
        }

        // If not found in recent searches cache, try a direct search
        // This will perform a fresh search which might find the festival

        //TODO add get festival call here
        val allFestivals = searchInteractor.searchFestivals(
            query = "",
            statusFilters = listOf("upcoming", "ongoing", "completed"),
            limit = 100
        )

        return allFestivals.find { it.id == festivalId }
    }
}
