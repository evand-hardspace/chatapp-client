package com.evandhardspace.core.domain.util

sealed interface Result<out E : DomainError, out S> {
    value class Success<out S>(val data: S) : Result<Nothing, S>
    value class Failure<out E : DomainError>(val error: E) : Result<E, Nothing>
}

interface DomainError

@PublishedApi
internal class ErrorThrowable(val error: DomainError) : Throwable()

inline fun <T, E : DomainError, R> Result<E, T>.map(map: (T) -> R): Result<E, R> = when (this) {
    is Result.Failure -> Result.Failure(error)
    is Result.Success -> Result.Success(map(this.data))
}

inline fun <T, E : DomainError> Result<E, T>.onSuccess(action: (T) -> Unit): Result<E, T> = when (this) {
    is Result.Failure -> this
    is Result.Success -> {
        action(this.data)
        this
    }
}

inline fun <T, E : DomainError> Result<E, T>.onFailure(action: (E) -> Unit): Result<E, T> = when (this) {
    is Result.Failure -> {
        action(error)
        this
    }

    is Result.Success -> this
}

fun <E : DomainError, T> Result<E, T>.asEmptyResult(): EmptyResult<E> = map { }

fun <E : DomainError, T> E.asFailure(): Result<E, T> = Result.Failure(this)
fun <E : DomainError, T> T.asSuccess(): Result<E, T> = Result.Success(this)

typealias EmptyResult<E> = Result<E, Unit>

// Experimental
@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError, T> result(body: ResultScope<E, T>.() -> T): Result<E, T> = try {
    body(ResultScope()).asSuccess()
} catch (t: ErrorThrowable) {
    Result.Failure(t.error as E)
}

inline fun <E : DomainError> emptyResult(body: ResultScope<E, Unit>.() -> Unit): EmptyResult<E> =
    result(body)


class ResultScope<E : DomainError, T>

context(_: ResultScope<E, T>)
fun <E : DomainError, T> E.raise(): Nothing =
    throw ErrorThrowable(this)

