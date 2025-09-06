package com.org.basshead.feature.avatar.interactor

import com.org.basshead.feature.avatar.model.Avatar
import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.interactor.UserProfileKey
import com.org.basshead.utils.interactor.Interactor
import com.org.basshead.utils.interactor.invalidateCache
import com.org.basshead.utils.interactor.withInteractorContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.auth.auth

interface AvatarInteractor: Interactor {
    suspend fun getAvatars(): List<Avatar>
    suspend fun updateUserAvatar(avatarUrl: String)
    suspend fun getCurrentUserAvatarUrl(): String?
}

class AvatarInteractorImpl(
    private val supabaseClient: SupabaseClient,
    private val dashBoardInteractor: DashBoardInteractor
) : AvatarInteractor {

    override suspend fun getAvatars(): List<Avatar> {
        return supabaseClient.from("avatars")
            .select()
            .decodeList<Avatar>()
    }

    override suspend fun updateUserAvatar(avatarUrl: String)  {
        withInteractorContext {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User not authenticated")

            supabaseClient.from("profiles")
                .update(
                    mapOf("avatar_url" to avatarUrl)
                ) {
                    filter {
                        eq("id", userId)
                    }
                }
            invalidateCache(UserProfileKey)
        }
    }

    override suspend fun getCurrentUserAvatarUrl(): String? {
        return dashBoardInteractor.getUserProfile()?.avatarUrl
    }
}
