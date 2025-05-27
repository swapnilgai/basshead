package com.org.basshead.utils.interactor

import com.org.basshead.utils.cache.CacheOptions
import com.org.basshead.utils.cache.LRUCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

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

suspend fun <T>Interactor.withInteractorContext(
    cacheOption: CacheOptions? = null,
    forceRefresh: Boolean = false,
    retryOption: RetryOption = RetryOption(retryCount = 0),
    block: suspend () -> T,
): T {
    val cache = LRUCache.create(1000)

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
            var attemptIndex = -1
            var blockResult: T

            while (true) {
                try {
                    if (attemptIndex > 0) {
                        val duration = retryOption.initialDelay * retryOption.delayIncrementalFactor * attemptIndex
                        delay(duration.toLong())
                    }

                    blockResult = coroutineScope {
                        block()
                    }

                    cacheOption?.run {
                        if (allowOverwrite) {
                            cache.set(
                                key = cacheOption.key,
                                secondaryKey = cacheOption.secondaryKey,
                                value = blockResult,
                            )
                        }
                    }
                    break
                } catch (e: Exception) {
                    if (retryOption.retryCount > attemptIndex) {
                        attemptIndex++
                        continue
                    }
                    coroutineContext.ensureActive()
                    throw e.toInteractorException()
                }
            }
            blockResult
        }
    }
}
