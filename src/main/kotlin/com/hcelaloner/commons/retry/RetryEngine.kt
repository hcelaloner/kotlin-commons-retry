package com.hcelaloner.commons.retry

import kotlinx.coroutines.delay
import java.time.Duration

internal suspend fun <T> retryAsync(
    action: suspend () -> T,
    retryableExceptions: Set<Class<out Throwable>>,
    onRetry: (suspend (Throwable, Duration, Int) -> Unit)?,
    maxRetryCount: Int,
    waitDurationGenerator: ((Int) -> Duration)
): T {
    var retryCount = 0
    while (true) {
        try {
            return action()
        } catch (t: Throwable) {
            val shouldRetryException = retryableExceptions.any { it.isInstance(t) }
            if (!shouldRetryException) {
                throw t
            }

            if (retryCount >= maxRetryCount) {
                throw t
            }
            retryCount += 1

            val waitDuration = waitDurationGenerator(retryCount)
            onRetry?.invoke(t, waitDuration, retryCount)

            delay(waitDuration.toMillis())
        }
    }
}

internal fun <T> retry(
    action: () -> T,
    retryableExceptions: Set<Class<out Throwable>>,
    onRetry: ((Throwable, Duration, Int) -> Unit)?,
    maxRetryCount: Int,
    waitDurationGenerator: ((Int) -> Duration)
): T {
    var retryCount = 0
    while (true) {
        try {
            return action()
        } catch (t: Throwable) {
            val shouldRetryException = retryableExceptions.any { it.isInstance(t) }
            if (!shouldRetryException) {
                throw t
            }

            if (retryCount >= maxRetryCount) {
                throw t
            }
            retryCount += 1

            val waitDuration = waitDurationGenerator(retryCount)
            onRetry?.invoke(t, waitDuration, retryCount)

            Thread.sleep(waitDuration.toMillis())
        }
    }
}