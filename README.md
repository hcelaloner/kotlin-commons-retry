# Kotlin Commons Retry

This project aims to provide retry support for java and kotlin applications.

## Usage

```kotlin
// Retry once with no delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retry()

policy.execute { action() }

// Retry once with fixed delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .fixedDelay(Duration.ofMillis(100))
    .retry()

policy.execute { action() }

// Retry once with exponential delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .exponentialDelay(2, Duration.ofMillis(100), Duration.ofMillis(500))
    .retry()

policy.execute { action() }

// Retry once with custom delay generator if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .customDelay { 
        retryCount -> // some code that returns Duration based on retryCount 
    }
    .retry()

policy.execute { action() }

// Retry five times with no delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retry(5)

policy.execute { action() }

// Retry five times with no delay and onRetry callback if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retry(5) {
        throwable, duration, retryCount -> // log
    }

policy.execute { action() }
```

## Usage With Coroutines

```kotlin
// Retry once with no delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retryAsync()

policy.execute { suspendingAction() }

// Retry five times with no delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retryAsync(5)

policy.execute { suspendingAction() }

// Retry once with fixed delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .fixedDelay(Duration.ofMillis(100))
    .retryAsync()

policy.execute { suspendingAction() }

// Retry once with exponential delay if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .exponentialDelay(2, Duration.ofMillis(100), Duration.ofMillis(500))
    .retryAsync()

policy.execute { suspendingAction() }

// Retry once with custom delay generator if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .customDelay { 
        retryCount -> // some code that returns Duration based on retryCount 
    }
    .retryAsync()

policy.execute { suspendingAction() }

// Retry five times with  no delay and onRetry callback if RuntimeException occurs
val policy = PolicyBuilder()
    .handle(RuntimeException::class.java)
    .noDelay()
    .retryAsync(5) {
        throwable, duration, retryCount -> // log
    }

policy.execute { suspendingAction() }
```

## Inspiration

Heavily developed based on the fluent interface provided by [Polly](https://github.com/App-vNext/Polly)