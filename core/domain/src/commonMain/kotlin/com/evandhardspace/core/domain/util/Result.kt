package com.evandhardspace.core.domain.util

sealed interface Result<out E : DomainError, out S> {
    data class Success<out S>(val data: S) : Result<Nothing, S>
    data class Failure<out E : DomainError>(val error: E) : Result<E, Nothing>
}

interface DomainError

@PublishedApi
internal class ErrorThrowable(val error: DomainError) : Throwable()

inline fun <T, E : DomainError, R> Result<E, T>.map(map: (T) -> R): Result<E, R> = when (this) {
    is Result.Failure -> Result.Failure(error)
    is Result.Success -> Result.Success(map(this.data))
}

inline fun <T, E : DomainError> Result<E, T>.onSuccess(action: (T) -> Unit): Result<E, T> =
    when (this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(this.data)
            this
        }
    }

inline fun <T, E : DomainError> Result<E, T>.onFailure(action: (E) -> Unit): Result<E, T> =
    when (this) {
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
typealias ErrorResult<E> = Result<E, Nothing>

// Experimental
@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError, T> result(body: context(ResultScope<E, T>) () -> T): Result<E, T> =
    try {
        body(ResultScope).asSuccess()
    } catch (t: ErrorThrowable) {
        Result.Failure(t.error as E)
    }

inline fun <E : DomainError> emptyResult(body: context(ResultScope<E, Unit>) () -> Unit): EmptyResult<E> =
    result(body)

@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError> errorResult(body: ResultScope<E, Nothing>.() -> Nothing): ErrorResult<E> =
    try {
        body(ResultScope)
    } catch (t: ErrorThrowable) {
        Result.Failure(t.error as E)
    }


interface ResultScope<out E : DomainError, out T> {
    companion object : ResultScope<Nothing, Nothing>
}

context(_: ResultScope<E, T>)
inline fun <E : DomainError, T> E.raise(): Nothing =
    throw ErrorThrowable(this)

context(_: ResultScope<E, T>)
fun <E : DomainError, T> Result<E, T>.ensure(): T =
    when (this) {
        is Result.Failure<E> -> error.raise()
        is Result.Success<T> -> data
    }

