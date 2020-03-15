package com.hcelaloner.commons.retry

import java.time.Duration

class AsyncRetryPolicy internal constructor(
    private val maxRetryCount: Int,
    private val retryableExceptions: Set<Class<out Throwable>>,
    private val onRetry: (suspend (Throwable, Duration, Int) -> Unit)? = null,
    private val waitDurationGenerator: (Int) -> Duration
) {
    suspend fun <T> execute(action: suspend () -> T): T {
        return retryAsync(
            action,
            retryableExceptions,
            onRetry,
            maxRetryCount,
            waitDurationGenerator
        )
    }
}