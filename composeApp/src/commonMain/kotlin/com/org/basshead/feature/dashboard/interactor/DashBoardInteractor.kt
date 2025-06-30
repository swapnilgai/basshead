package com.org.basshead.feature.dashboard.interactor

import com.org.basshead.feature.dashboard.model.DailyHeadbang
import com.org.basshead.feature.dashboard.model.DailyHeadbangState
import com.org.basshead.feature.dashboard.model.FestivalSuggestion
import com.org.basshead.feature.dashboard.model.FestivalSuggestionState
import com.org.basshead.feature.dashboard.model.UserFestival
import com.org.basshead.feature.dashboard.model.UserFestivalState
import com.org.basshead.feature.dashboard.model.UserProfile
import com.org.basshead.feature.dashboard.model.UserProfileState
import com.org.basshead.feature.dashboard.model.toUiModel
import com.org.basshead.utils.cache.CacheKey
import com.org.basshead.utils.cache.CacheOptions
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
data class UserFestivalsPrimaryKey(val statuses: String) : CacheKey
data class UserFestivalsSecondaryKey(val lastSeenId: String? = null) : CacheKey
data class FestivalSuggestionsPrimaryKey(val statuses: String, val location: String?) : CacheKey
data class FestivalSuggestionsSecondaryKey(val lastSeenId: String?) : CacheKey

// Object-based CacheKey for user profile
object UserProfileKey : CacheKey {
    override fun toString(): String = "UserProfileKey"
}

// Interface
interface DashBoardInteractor : Interactor {
    suspend fun getDailyHeadbangs(
        startDate: String? = null,
        endDate: String? = null,
        limit: Int = 30,
    ): List<DailyHeadbangState>

    suspend fun getUserFestivals(
        statuses: List<String> = listOf("all"),
        limit: Int = 2,
        lastSeenId: String? = null,
        lastSeenStatus: String? = null,
        lastSeenTime: String? = null,
    ): List<UserFestivalState>

    suspend fun getFestivalSuggestions(
        statuses: List<String> = listOf("upcoming", "ongoing"),
        limit: Int = 3,
        lastSeenId: String? = null,
        location: String? = null,
    ): List<FestivalSuggestionState>

    suspend fun getUserProfile(): UserProfileState?
}

// Implementation
class DashBoardInteractorImpl(
    private val supabaseClient: SupabaseClient,
) : DashBoardInteractor {

    override suspend fun getDailyHeadbangs(
        startDate: String?,
        endDate: String?,
        limit: Int,
    ): List<DailyHeadbangState> = withInteractorContext(retryOption = RetryOption(retryCount = 2)) {
        val currentUser = supabaseClient.auth.currentUserOrNull()
            ?: throw Exception("Not authenticated")

        supabaseClient.postgrest.rpc(
            "get_user_daily_headbangs",
            parameters = buildJsonObject {
                put("_user_id", currentUser.id)
                startDate?.let { put("_start_date", it) }
                endDate?.let { put("_end_date", it) }
                put("_limit", limit)
            },
        ).decodeList<DailyHeadbang>()
            .map { networkModel ->
                DailyHeadbangState(
                    date = networkModel.date,
                    totalCount = networkModel.totalCount,
                    hasFestival = networkModel.hasFestival,
                )
            }
    }

    override suspend fun getUserFestivals(
        statuses: List<String>,
        limit: Int,
        lastSeenId: String?,
        lastSeenStatus: String?,
        lastSeenTime: String?,
    ): List<UserFestivalState> = withInteractorContext(
        cacheOption = CacheOptions(
            key = UserFestivalsPrimaryKey(statuses = statuses.toString()),
            secondaryKey = UserFestivalsSecondaryKey(lastSeenId = lastSeenId),
        ),
        retryOption = RetryOption(retryCount = 2),
    ) {
        val currentUser = supabaseClient.auth.currentUserOrNull()
            ?: throw Exception("Not authenticated")

        // Pass parameters directly without conversion
        supabaseClient.postgrest.rpc(
            "get_user_festivals",
            parameters = buildJsonObject {
                put("_user_id", currentUser.id)
                put("_status", JsonArray(statuses.map { JsonPrimitive(it) }))
                put("_limit", limit)
                lastSeenId?.let { put("_last_seen_id", it) }
                lastSeenStatus?.let { put("_last_seen_status", it) }
                lastSeenTime?.let { put("_last_seen_time", it) }
            },
        ).decodeList<UserFestival>()
            .map { networkModel ->
                UserFestivalState(
                    id = networkModel.id,
                    name = networkModel.name,
                    description = networkModel.description,
                    location = networkModel.location,
                    startTime = networkModel.startTime,
                    endTime = networkModel.endTime,
                    imageUrl = networkModel.imageUrl,
                    createdAt = networkModel.createdAt,
                    status = networkModel.status,
                    totalHeadbangs = networkModel.totalHeadbangs,
                    userRank = networkModel.userRank,
                    totalParticipants = networkModel.totalParticipants,
                )
            }
    }

    override suspend fun getFestivalSuggestions(
        statuses: List<String>,
        limit: Int,
        lastSeenId: String?,
        location: String?,
    ): List<FestivalSuggestionState> = withInteractorContext(
        cacheOption = CacheOptions(
            key = FestivalSuggestionsPrimaryKey(
                statuses = statuses.toString(),
                location = location,
            ),
            secondaryKey = FestivalSuggestionsSecondaryKey(lastSeenId = lastSeenId),
        ),
        retryOption = RetryOption(retryCount = 0),
    ) {
        val currentUser = supabaseClient.auth.currentUserOrNull()
            ?: throw Exception("Not authenticated")

        supabaseClient.postgrest.rpc(
            "get_festival_suggestions",
            parameters = buildJsonObject {
                put("_user_id", currentUser.id)
                put("_status", JsonArray(statuses.map { JsonPrimitive(it) }))
                put("_limit", limit)
                lastSeenId?.let { put("_last_seen_id", it) }
                location?.let { put("_location", it) }
            },
        ).decodeList<FestivalSuggestion>()
            .map { networkModel -> networkModel.toUiModel() }
    }

    override suspend fun getUserProfile(): UserProfileState? = withInteractorContext(
        cacheOption = CacheOptions(
            key = UserProfileKey
        ),
        retryOption = RetryOption(retryCount = 1)
    ) {
        val currentUser = supabaseClient.auth.currentUserOrNull()
            ?: return@withInteractorContext null

        supabaseClient.postgrest["profiles"]
            .select {
                filter {
                    eq("id", currentUser.id)
                }
            }
            .decodeSingle<UserProfile>()
            .toUiModel()
    }
}