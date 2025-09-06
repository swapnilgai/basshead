package com.org.basshead.utils.interactor

import com.org.basshead.utils.cache.CacheKey
import com.org.basshead.utils.cache.CacheOptions
import com.org.basshead.utils.cache.LRUCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.math.min
import kotlin.math.pow

object InteractorDispatcherProvider {
    internal val dispatcher: CoroutineDispatcher = Dispatchers.IO
}

data class RetryOption(
    val retryCount: Int,
    val initialDelay: Long = 100,
    val maxDelay: Long = 1000,
    val delayIncrementalFactor: Double = 2.0,
)

interface Interactor
private val cache = LRUCache.create(1000)

suspend fun <T>Interactor.withInteractorContext(
    cacheOption: CacheOptions? = null,
    forceRefresh: Boolean = false,
    retryOption: RetryOption = RetryOption(retryCount = 0),
    block: suspend () -> T,
): T {
    val context = InteractorDispatcherProvider.dispatcher
    return withContext(context) {
        val cacheResult = if (cacheOption != null && !forceRefresh) {
            cache.get<T>(key = cacheOption.key, secondaryKey = cacheOption.secondaryKey)
        } else {
            null
        }

        return@withContext if (cacheResult != null) {
            cacheResult
        } else {
            var attemptIndex = 0
            var blockResult: T

            while (true) {
                try {
                    if (attemptIndex > 0) {
                        val exponentialDelay = retryOption.initialDelay * retryOption.delayIncrementalFactor.pow(attemptIndex - 1)
                        val cappedDelay = min(exponentialDelay.toLong(), retryOption.maxDelay)
                        delay(cappedDelay)
                    }

                    blockResult = block()

                    cacheOption?.run {
                        if (allowOverwrite) {
                            cache.set(
                                key = key,
                                secondaryKey = secondaryKey,
                                value = blockResult,
                            )
                        }
                    }
                    break
                } catch (e: Exception) {
                    coroutineContext.ensureActive()
                    if (retryOption.retryCount > attemptIndex) {
                        attemptIndex++
                        continue
                    }
                    throw e.toInteractorException()
                }
            }
            blockResult
        }
    }
}

suspend fun Interactor.invalidateCache(cacheKey: CacheKey) = withContext(InteractorDispatcherProvider.dispatcher) {
    cache.remove(cacheKey)
}
