package com.hcelaloner.commons.retry

import java.time.Duration

class RetryPolicy internal constructor(
    private val maxRetryCount: Int,
    private val retryableExceptions: Set<Class<out Throwable>>,
    private val onRetry: ((Throwable, Duration, Int) -> Unit)? = null,
    private val waitDurationGenerator: (Int) -> Duration
) {
    fun <T> execute(action: () -> T): T {
        return retry(
            action,
            retryableExceptions,
            onRetry,
            maxRetryCount,
            waitDurationGenerator
        )
    }
}