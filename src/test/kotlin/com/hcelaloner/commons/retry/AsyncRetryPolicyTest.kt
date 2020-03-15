package com.hcelaloner.commons.retry

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class AsyncRetryPolicyTest {

    private interface FooAction {
        suspend fun action() {
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    internal fun `it should not retry any exception occurred during action if exception is not specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .noDelay()
            .retryAsync()

        val actionMock = mockk<FooAction>()
        coEvery { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { runBlockingTest { policy.execute { actionMock.action() } } }

        // Then
        coVerify(exactly = 1) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    internal fun `it should retry once if an exception occurred during action and it is specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .handle(RuntimeException::class.java)
            .noDelay()
            .retryAsync()

        val actionMock = mockk<FooAction>()
        coEvery { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { runBlockingTest { policy.execute { actionMock.action() } } }

        // Then
        coVerify(exactly = 2) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    internal fun `it should retry specified number of times if an exception occurred during action and it is specified for retry`() {
        // Given
        val policy = PolicyBuilder()
            .handle(RuntimeException::class.java)
            .noDelay()
            .retryAsync(5)

        val actionMock = mockk<FooAction>()
        coEvery { actionMock.action() } throws RuntimeException()

        // When
        val exception = catchThrowable { runBlockingTest { policy.execute { actionMock.action() } } }

        // Then
        coVerify(exactly = 6) { actionMock.action() }

        assertThat(exception).isNotNull()
            .isInstanceOf(RuntimeException::class.java)
    }
}