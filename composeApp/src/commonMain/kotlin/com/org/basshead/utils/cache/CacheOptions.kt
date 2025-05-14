package com.org.basshead.utils.cache

data class CacheOptions(
    val key: CacheKey,
    val secondaryKey: CacheKey? = null,
    val expirationPolicy: CacheExpirationPolicy = defaultCacheExpiration,
    val allowOverwrite: Boolean = true,
)

fun CacheOptions(
    key: String,
    secondaryKey: String? = null,
    expirationPolicy: CacheExpirationPolicy = defaultCacheExpiration,
): CacheOptions {
    return CacheOptions(
        key = validateCacheKey(key),
        secondaryKey = secondaryKey?.let { validateCacheKey(it) },
        expirationPolicy = expirationPolicy,
    )
}

private fun validateCacheKey(key: String): CacheKey {
    return if (key.isNotEmpty() && key.isNotBlank()) {
        StringCacheKey(key)
    } else {
        throw IllegalArgumentException("Cache key must not be empty or blank")
    }
}
