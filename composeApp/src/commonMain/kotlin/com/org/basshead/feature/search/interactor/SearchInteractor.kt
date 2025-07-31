package com.org.basshead.feature.search.interactor

import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.FestivalSuggestion
import com.org.basshead.feature.dashboard.model.toFestivalItemState
import com.org.basshead.utils.cache.CacheKey
import com.org.basshead.utils.cache.CacheOptions
import com.org.basshead.utils.cache.shortCacheExpiration
import com.org.basshead.utils.interactor.Interactor
import com.org.basshead.utils.interactor.RetryOption
import com.org.basshead.utils.interactor.withInteractorContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Cache Keys
data class SearchFestivalsPrimaryKey(
    val query: String,
) : CacheKey

data class SearchFestivalsSecondaryKey(
    val limit: Int,
    val statusFilters: List<String>,
    val locationFilter: String?,
    val lastSeenId: String?,
) : CacheKey

data class SearchFestivalsSearchKey(
    val key: String = "SearchFestivalsSearchKey",
) : CacheKey

data class SaveSearchCacheResult(
    val query: String,
    val locationFilter: String?,
    val statusFilters: List<String>?,
    val limit: Int,
    val lastSeenId: String?,
)

// Interface
interface SearchInteractor : Interactor {
    suspend fun searchFestivals(
        query: String = "",
        statusFilters: List<String> = listOf("upcoming", "ongoing"),
        locationFilter: String? = null,
        limit: Int = 20,
        lastSeenId: String? = null, // Changed from offset to lastSeenId
    ): List<FestivalItemState>

    suspend fun getRecentSearchesCacheKey(): List<SaveSearchCacheResult>

    suspend fun saveSearchQueryCacheKey(query: String, locationFilter: String?, limit: Int, statusFilters: List<String> = emptyList(), lastSeenId: String?): List<SaveSearchCacheResult>

    suspend fun clearSearchHistoryCacheKey()
}

// Implementation
class SearchInteractorImpl(
    private val supabaseClient: SupabaseClient,
) : SearchInteractor {

    override suspend fun searchFestivals(
        query: String,
        statusFilters: List<String>,
        locationFilter: String?,
        limit: Int,
        lastSeenId: String?, // Changed from offset to lastSeenId
    ): List<FestivalItemState> = withInteractorContext(
        cacheOption = CacheOptions(
            key = SearchFestivalsPrimaryKey(
                query = query.trim().lowercase(),
            ),
            secondaryKey = SearchFestivalsSecondaryKey(
                locationFilter = locationFilter?.trim()?.lowercase(),
                lastSeenId = lastSeenId,
                statusFilters = statusFilters,
                limit = limit,
            ),
            expirationPolicy = shortCacheExpiration, // 5 minutes - search results change frequently
        ),
        retryOption = RetryOption(retryCount = 1),
    ) {
        val currentUser = supabaseClient.auth.currentUserOrNull()
            ?: throw Exception("Not authenticated")

        // Use get_festival_suggestions with search support (shows only non-joined festivals)

        // Handle empty status filters - default to upcoming and ongoing
        val effectiveStatusFilters = if (statusFilters.isEmpty()) {
            listOf("upcoming", "ongoing")
        } else {
            statusFilters.distinct() // Remove duplicates
        }

        // Clean location filter - only pass if not null and not blank
        // Enhanced location search supports:
        // - City names: "Miami", "Denver", "Los Angeles"
        // - State codes: "FL", "CA", "NY"
        // - Venue names: "Red Rocks", "Madison Square Garden"
        // - Partial matching: "Los Ang" matches "Los Angeles, CA"
        // - Fuzzy matching with similarity scoring
        val effectiveLocationFilter = locationFilter?.trim()?.takeIf { it.isNotBlank() }

        // Clean search query
        val effectiveQuery = query.trim()

        // Validate limit
        val effectiveLimit = limit.coerceIn(1, 100) // Ensure reasonable bounds

        val results = supabaseClient.postgrest.rpc(
            function = "get_festival_suggestions",
            parameters = buildJsonObject {
                put("_user_id", currentUser.id)
                put("_status", JsonArray(effectiveStatusFilters.map { JsonPrimitive(it) }))
                put("_search_query", effectiveQuery)
                effectiveLocationFilter?.let { put("_location", it) }
                put("_limit", effectiveLimit)
                lastSeenId?.let {
                    put(
                        "_last_seen_id",
                        it,
                    )
                } // Use lastSeenId for cursor-based pagination
            },
        ).decodeList<FestivalSuggestion>()

        saveSearchQueryCacheKey(query = query, locationFilter = locationFilter, limit = limit, statusFilters = statusFilters, lastSeenId = lastSeenId)

        results.map { it.toFestivalItemState() }
    }

    override suspend fun getRecentSearchesCacheKey(): List<SaveSearchCacheResult> = withInteractorContext(
        cacheOption = CacheOptions(
            key = SearchFestivalsSearchKey(),
            expirationPolicy = shortCacheExpiration,
        ),
    ) {
        emptyList<SaveSearchCacheResult>()
    }

    override suspend fun saveSearchQueryCacheKey(query: String, locationFilter: String?, limit: Int, statusFilters: List<String>, lastSeenId: String?): List<SaveSearchCacheResult> {
        return withInteractorContext(
            cacheOption = CacheOptions(
                key = SearchFestivalsSearchKey(),
                expirationPolicy = shortCacheExpiration,
            ),
            forceRefresh = true,
        ) {
            val savedSearch: MutableList<SaveSearchCacheResult> = getRecentSearchesCacheKey().toMutableList()
            savedSearch.add(
                SaveSearchCacheResult(
                    query = query,
                    locationFilter = locationFilter,
                    limit = limit,
                    statusFilters = statusFilters,
                    lastSeenId = lastSeenId,
                ),
            )
            savedSearch
        }
    }

    override suspend fun clearSearchHistoryCacheKey() {
        withInteractorContext(
            cacheOption = CacheOptions(
                key = SearchFestivalsSearchKey(),
                expirationPolicy = shortCacheExpiration,
            ),
            forceRefresh = true,
        ) {
            emptyList<SaveSearchCacheResult>()
        }
    }
}
