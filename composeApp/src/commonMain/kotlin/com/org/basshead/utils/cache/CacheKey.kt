package com.org.basshead.utils.cache

interface CacheKey

data class StringCacheKey(val str: String) : CacheKey
