package com.hcelaloner.commons.retry

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

internal class FixedDelayGeneratorTest {

    @Test
    internal fun `it should not accept negative delay`() {
        // Given

        // When
        val exception = Assertions.catchThrowable {
            FixedDelayGenerator(Duration.ofSeconds(-1))
        }

        // Then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Fixed delay must be greater than or equal to zero")
    }

    @Test
    internal fun `it should accept zero delay`() {
        // Given
        val fixedDelayGenerator = FixedDelayGenerator(Duration.ZERO)

        // When
        val delay = fixedDelayGenerator(1)

        // Then
        assertThat(delay).isZero()
    }

    @Test
    internal fun `it should generate fixed delay`() {
        // Given
        val fixedDelayGenerator = FixedDelayGenerator(Duration.ofSeconds(1))

        // When
        val firstDelay = fixedDelayGenerator(0)
        val secondDelay = fixedDelayGenerator(1)
        val thirdDelay = fixedDelayGenerator(2)

        // Then
        assertThat(firstDelay).hasSeconds(1)
        assertThat(secondDelay).hasSeconds(1)
        assertThat(thirdDelay).hasSeconds(1)
    }
}