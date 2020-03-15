package com.hcelaloner.commons.retry

import java.time.Duration

internal class FixedDelayGenerator(private val fixedDelay: Duration) : (Int) -> Duration {
    init {
        require(!fixedDelay.isNegative) {
            "Fixed delay must be greater than or equal to zero"
        }
    }

    override fun invoke(retryCount: Int): Duration = fixedDelay

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is FixedDelayGenerator) {
            return false
        }

        return fixedDelay == other.fixedDelay
    }

    override fun hashCode(): Int {
        return fixedDelay.hashCode()
    }

    override fun toString(): String {
        return "FixedDelayGenerator(fixedDelay=$fixedDelay)"
    }
}