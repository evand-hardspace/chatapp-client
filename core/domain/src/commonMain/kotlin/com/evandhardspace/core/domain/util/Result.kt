package com.evandhardspace.core.domain.util

sealed interface Result<out E : DomainError, out S> {
    data class Success<out S>(val data: S) : Result<Nothing, S>
    data class Failure<out E : DomainError>(val error: E) : Result<E, Nothing>
}

interface DomainError

class ResultScope<E : DomainError, T> @PublishedApi internal constructor()

@PublishedApi
internal class ErrorThrowable(val error: DomainError) : Throwable()

inline fun <T, E : DomainError, R> Result<E, T>.map(
    map: (T) -> R,
): Result<E, R> = when (this) {
    is Result.Failure -> Result.Failure(error)
    is Result.Success -> Result.Success(map(data))
}

inline fun <T, E1 : DomainError, E2 : DomainError> Result<E1, T>.mapFailure(
    map: (E1) -> E2,
): Result<E2, T> = when (this) {
    is Result.Failure -> Result.Failure(map(error))
    is Result.Success -> Result.Success(data)
}

inline fun <T, E : DomainError> Result<E, T>.onSuccess(
    action: (T) -> Unit,
): Result<E, T> = when (this) {
    is Result.Success -> {
        action(this.data)
        this
    }
    is Result.Failure -> this
}

inline fun <T, E : DomainError> Result<E, T>.onFailure(
    action: (E) -> Unit,
): Result<E, T> = when (this) {
    is Result.Failure -> {
        action(error)
        this
    }

    is Result.Success -> this
}

inline fun <T, E : DomainError, R> Result<E, T>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Failure -> onFailure(error)
}

fun <T, E : DomainError> Result<E, T>.getOrNull(): T? = when (this) {
    is Result.Success<T> -> data
    is Result.Failure<E> -> null
}

fun <T, E : DomainError> Result<E, T>.getOrDefault(
    defaultValue: (E) -> T,
): T? = when (this) {
    is Result.Success<T> -> data
    is Result.Failure<E> -> defaultValue(error)
}

fun <E : DomainError, T> Result<E, T>.asEmptyResult(): EmptyResult<E> = map { }

fun <E : DomainError, T> E.asFailure(): Result<E, T> = Result.Failure(this)
fun <E : DomainError, T> T.asSuccess(): Result<E, T> = Result.Success(this)

typealias EmptyResult<E> = Result<E, Unit>
typealias ErrorResult<E> = Result<E, Nothing>

// Experimental
@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError, T> result(
    body: context(ResultScope<E, T>) () -> T,
): Result<E, T> = try {
    body(ResultScope()).asSuccess()
} catch (t: ErrorThrowable) {
    Result.Failure(t.error as E)
}

inline fun <E : DomainError> emptyResult(body: context(ResultScope<E, Unit>) () -> Unit): EmptyResult<E> =
    result(body)

@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError> errorResult(
    body: context(ResultScope<E, Nothing>) () -> Nothing,
): ErrorResult<E> = try {
    body(ResultScope())
} catch (t: ErrorThrowable) {
    Result.Failure(t.error as E)
}

context(_: ResultScope<E, T>)
fun <E : DomainError, T> E.raise(): Nothing = throw ErrorThrowable(this)

context(_: ResultScope<E, T>)
fun <E : DomainError, T> Result<E, T>.ensure(): T = when (this) {
    is Result.Failure<E> -> error.raise()
    is Result.Success<T> -> data
}
