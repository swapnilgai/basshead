package com.org.basshead.utils.cache

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.datetime.Clock

class LRUCache private constructor(private val maxSize: Int) {

    // Atomic reference to hold the cache, shared across platforms.
    private val cache = atomic(LinkedHashMap<CacheKey, Any>(maxSize, 0.75f))

    // Set a cache entry with optional secondary key and expiration policy
    suspend fun set(
        key: CacheKey,
        secondaryKey: CacheKey? = null,
        expirationPolicy: CacheExpirationPolicy = defaultCacheExpiration,
        requestTimestamp: Long = Clock.System.now().toEpochMilliseconds(),
        value: Any?,
    ) {
        // Update the cache atomically
        cache.update { currentCache ->
            val updatedCache = currentCache.toMutableMap() as LinkedHashMap<CacheKey, Any>

            if (secondaryKey == null) {
                if (value == null) {
                    updatedCache.remove(key)
                } else {
                    updatedCache[key] = CacheEntry(value, requestTimestamp, expirationPolicy)
                }
            } else {
                val secondaryMap = updatedCache.remove(key) as? LinkedHashMap<CacheKey, CacheEntry>
                    ?: mutableMapOf()

                if (value == null) {
                    secondaryMap.remove(secondaryKey)
                } else {
                    secondaryMap[secondaryKey] = CacheEntry(value, requestTimestamp, expirationPolicy)
                }
                updatedCache[key] = secondaryMap
            }

            // Enforce size limit
            if (updatedCache.size > maxSize) {
                val eldestKey = updatedCache.keys.firstOrNull()
                updatedCache.remove(eldestKey)
            }

            updatedCache
        }
    }

    suspend fun <T> get(key: CacheKey, secondaryKey: CacheKey? = null): T? {
        var result: T? = null

        cache.update { currentCache ->
            val updatedCache = currentCache.toMutableMap() as LinkedHashMap<CacheKey, Any>

            if (secondaryKey == null) {
                val entry = updatedCache.remove(key) as? CacheEntry
                if (entry != null && !entry.isExpired) {
                    // Reinsert to update access order
                    updatedCache[key] = entry
                    result = entry.data as T
                }
            } else {
                val map = updatedCache.remove(key) as? Map<CacheKey, CacheEntry>
                val subEntry = map?.get(secondaryKey)
                if (map != null && subEntry != null && !subEntry.isExpired) {
                    // Reinsert the whole map to update parent key access
                    updatedCache[key] = map
                    result = subEntry.data as T
                } else if (map != null) {
                    // Reinsert anyway, even if no valid entry — preserves access behavior
                    updatedCache[key] = map
                }
            }

            updatedCache
        }

        return result
    }

    // Remove a cache entry by its key
    suspend fun remove(key: CacheKey): Any? {
        val currentCache = cache.value
        return currentCache.remove(key)
    }

    // Clear the entire cache
    suspend fun clear() {
        cache.update { LinkedHashMap<CacheKey, Any>() }
    }

    // Cache entry data class
    private data class CacheEntry(
        val data: Any,
        val requestTimestamp: Long,
        val expirationPolicy: CacheExpirationPolicy,
    ) {
        val isExpired: Boolean
            get() = expirationPolicy.isEntryExpired(requestTimestamp)
    }

    // Companion object for easy cache creation
    companion object {
        fun create(maxSize: Int): LRUCache = LRUCache(maxSize)
    }
}
