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
    val statusFilters: List<String>,
    val locationFilter: String?,
) : CacheKey

data class SearchFestivalsSecondaryKey(
    val limit: Int,
    val lastSeenId: String?, // Changed from offset to lastSeenId
) : CacheKey

// Interface
interface SearchInteractor : Interactor {
    suspend fun searchFestivals(
        query: String = "",
        statusFilters: List<String> = listOf("upcoming", "ongoing"),
        locationFilter: String? = null,
        limit: Int = 20,
        lastSeenId: String? = null, // Changed from offset to lastSeenId
    ): List<FestivalItemState>

    suspend fun getRecentSearches(): List<String>

    suspend fun saveSearchQuery(query: String)

    suspend fun clearSearchHistory()
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
                statusFilters = statusFilters.sorted(),
                locationFilter = locationFilter?.trim()?.lowercase(),
            ),
            secondaryKey = SearchFestivalsSecondaryKey(
                limit = limit,
                lastSeenId = lastSeenId,
            ),
            expirationPolicy = shortCacheExpiration, // 5 minutes - search results change frequently
        ),
        retryOption = RetryOption(retryCount = 2),
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
                lastSeenId?.let { put("_last_seen_id", it) } // Use lastSeenId for cursor-based pagination
            },
        ).decodeList<FestivalSuggestion>()

        results.map { it.toFestivalItemState() }
    }

    override suspend fun getRecentSearches(): List<String> = withInteractorContext(
        cacheOption = CacheOptions(
            key = object : CacheKey {
                override fun toString(): String = "recent_searches"
            },
            expirationPolicy = shortCacheExpiration,
        ),
        retryOption = RetryOption(retryCount = 0),
    ) {
        // For now, return empty list. In a real app, this would query local storage
        // or a user preferences table in Supabase
        emptyList()
    }

    override suspend fun saveSearchQuery(query: String) = withInteractorContext(
        retryOption = RetryOption(retryCount = 0),
    ) {
        // For now, no-op. In a real app, this would save to local storage
        // or a user preferences table in Supabase
        Unit
    }

    override suspend fun clearSearchHistory() = withInteractorContext(
        retryOption = RetryOption(retryCount = 0),
    ) {
        // For now, no-op. In a real app, this would clear local storage
        // or user preferences table in Supabase
        Unit
    }
}
