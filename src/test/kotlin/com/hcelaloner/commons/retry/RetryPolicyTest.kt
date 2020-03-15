package com.hcelaloner.commons.retry

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class RetryPolicyTest {

    private interface FooAction {
        fun action() {
        }
    }

    @Test
    internal fun `it should not retry any exception occurred during action if exception is not specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .noDelay()
            .retry()

        val actionMock = mockk<FooAction>()
        every { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { policy.execute { actionMock.action() } }

        // Then
        verify(exactly = 1) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }

    @Test
    internal fun `it should retry once if an exception occurred during action and it is specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .handle(RuntimeException::class.java)
            .noDelay()
            .retry()

        val actionMock = mockk<FooAction>()
        every { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { policy.execute { actionMock.action() } }

        // Then
        verify(exactly = 2) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }

    @Test
    internal fun `it should retry specified number of times if an exception occurred during action and it is specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .handle(RuntimeException::class.java)
            .noDelay()
            .retry(5)

        val actionMock = mockk<FooAction>()
        every { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { policy.execute { actionMock.action() } }

        // Then
        verify(exactly = 6) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }
}