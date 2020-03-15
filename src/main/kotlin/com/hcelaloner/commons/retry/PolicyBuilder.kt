package com.hcelaloner.commons.retry

import java.time.Duration

class PolicyBuilder {
    private val retryableExceptions: MutableSet<Class<out Throwable>> = mutableSetOf()
    private var waitDurationGenerator: ((Int) -> Duration) = FixedDelayGenerator(Duration.ZERO)

    fun handle(retryableException: Class<out Throwable>): PolicyBuilder {
        this.retryableExceptions.add(retryableException)
        return this
    }

    fun handle(retryableExceptions: Collection<Class<out Throwable>>): PolicyBuilder {
        this.retryableExceptions.addAll(retryableExceptions)
        return this
    }

    fun noDelay(): PolicyBuilder {
        this.waitDurationGenerator = FixedDelayGenerator(Duration.ZERO)
        return this
    }

    fun fixedDelay(duration: Duration): PolicyBuilder {
        this.waitDurationGenerator = FixedDelayGenerator(duration)
        return this
    }

    fun exponentialDelay(factor: Int, firstDelay: Duration, maxDelay: Duration): PolicyBuilder {
        this.waitDurationGenerator = ExponentialDelayGenerator(factor, firstDelay, maxDelay)
        return this
    }

    fun customDelay(waitDurationGenerator: (Int) -> Duration): PolicyBuilder {
        this.waitDurationGenerator = waitDurationGenerator
        return this
    }

    @JvmOverloads
    fun retryAsync(
        maxRetryCount: Int = 1,
        onRetry: (suspend (Throwable, Duration, Int) -> Unit)? = null
    ): AsyncRetryPolicy {
        return AsyncRetryPolicy(
            maxRetryCount,
            retryableExceptions.toSet(),
            onRetry,
            waitDurationGenerator
        )
    }

    @JvmOverloads
    fun retry(maxRetryCount: Int = 1, onRetry: ((Throwable, Duration, Int) -> Unit)? = null): RetryPolicy {
        return RetryPolicy(
            maxRetryCount,
            retryableExceptions.toSet(),
            onRetry,
            waitDurationGenerator
        )
    }
}