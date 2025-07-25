package com.org.basshead.utils.cache

import kotlinx.datetime.Clock

interface CacheExpirationPolicy {
    fun isEntryExpired(requestTimeStamp: Long): Boolean
}

data class ExpireAfterTimeout(val timeoutDurationMillis: Long) : CacheExpirationPolicy {
    override fun isEntryExpired(requestTimeStamp: Long): Boolean {
        val now = Clock.System.now().toEpochMilliseconds()
        return now >= requestTimeStamp + timeoutDurationMillis
    }
}

private const val DEFAULT_CACHE_EXPIRATION_DURATION = 1000L * 60 * 15 // 15 minutes
private const val SHORT_CACHE_EXPIRATION_DURATION = 1000L * 60 * 5 // 5 minutes
private const val LONG_CACHE_EXPIRATION_DURATION = 1000L * 60 * 30 // 30 minutes

val defaultCacheExpiration = ExpireAfterTimeout(DEFAULT_CACHE_EXPIRATION_DURATION)
val shortCacheExpiration = ExpireAfterTimeout(SHORT_CACHE_EXPIRATION_DURATION)
val longCacheExpiration = ExpireAfterTimeout(LONG_CACHE_EXPIRATION_DURATION)
