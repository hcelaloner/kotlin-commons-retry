package com.hcelaloner.commons.retry

import java.time.Duration
import kotlin.math.pow
import kotlin.math.roundToLong

internal class ExponentialDelayGenerator(
    private val factor: Int,
    private val firstDelay: Duration,
    private val maxDelay: Duration
) : (Int) -> Duration {
    init {
        require(factor >= 0) {
            "Factor must be greater than or equal to zero"
        }

        require(!firstDelay.isZero && !firstDelay.isNegative) {
            "First delay must be greater than zero"
        }

        require(!maxDelay.isZero && !maxDelay.isNegative) {
            "Max delay must be greater than zero"
        }

        require(maxDelay >= firstDelay) {
            "Max delay must be greater than or equal to first delay"
        }
    }

    override fun invoke(retryCount: Int): Duration {
        val nextDelay = firstDelay.multipliedBy(factor.toDouble().pow(retryCount).roundToLong())
        return if (nextDelay > maxDelay) {
            maxDelay
        } else {
            nextDelay
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is ExponentialDelayGenerator) {
            return false
        }

        return factor == other.factor && firstDelay == other.firstDelay && maxDelay == other.maxDelay
    }

    override fun hashCode(): Int {
        var result = factor
        result = 31 * result + firstDelay.hashCode()
        result = 31 * result + maxDelay.hashCode()
        return result
    }

    override fun toString(): String {
        return "ExponentialDelayGenerator(factor=$factor, firstDelay=$firstDelay, maxDelay=$maxDelay)"
    }
}