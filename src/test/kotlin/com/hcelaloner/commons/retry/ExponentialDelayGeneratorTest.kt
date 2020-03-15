package com.hcelaloner.commons.retry

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Duration

internal class ExponentialDelayGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = [-100, -10, -1])
    internal fun `factor must be greater than or equal to zero`(factor: Int) {
        // Given

        // When
        val exception = catchThrowable {
            ExponentialDelayGenerator(factor, Duration.ofMillis(100), Duration.ofMillis(500))
        }

        // Then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Factor must be greater than or equal to zero")
    }

    @ParameterizedTest
    @ValueSource(longs = [-100, -10, -1, 0])
    internal fun `firstDelay must be greater than zero`(firstDelayInMillis: Long) {
        // Given

        // When
        val exception = catchThrowable {
            ExponentialDelayGenerator(2, Duration.ofMillis(firstDelayInMillis), Duration.ofMillis(500))
        }

        // Then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("First delay must be greater than zero")
    }

    @ParameterizedTest
    @ValueSource(longs = [-100, -10, -1, 0])
    internal fun `maxDelay must be greater than zero`(maxDelayInMillis: Long) {
        // Given

        // When
        val exception = catchThrowable {
            ExponentialDelayGenerator(2, Duration.ofMillis(100), Duration.ofMillis(maxDelayInMillis))
        }

        // Then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Max delay must be greater than zero")
    }

    @Test
    internal fun `maxDelay must be greater than or equal to firstDelay`() {
        // Given

        // When
        val exception = catchThrowable {
            ExponentialDelayGenerator(2, Duration.ofMillis(100), Duration.ofMillis(50))
        }

        // Then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Max delay must be greater than or equal to first delay")
    }

    @Test
    internal fun `it should generate exponential delays`() {
        // Given
        val exponentialDelayGenerator = ExponentialDelayGenerator(2, Duration.ofMillis(100), Duration.ofMillis(500))

        // When
        val firstDelay = exponentialDelayGenerator(0)
        val secondDelay = exponentialDelayGenerator(1)
        val thirdDelay = exponentialDelayGenerator(2)
        val fourthDelay = exponentialDelayGenerator(3)
        val fifthDelay = exponentialDelayGenerator(4)
        val sixthDelay = exponentialDelayGenerator(5)

        // Then
        assertThat(firstDelay).hasMillis(100)
        assertThat(secondDelay).hasMillis(200)
        assertThat(thirdDelay).hasMillis(400)
        assertThat(fourthDelay).hasMillis(500)
        assertThat(fifthDelay).hasMillis(500)
        assertThat(sixthDelay).hasMillis(500)
    }
}