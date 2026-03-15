package com.evandhardspace.core.domain.util

sealed interface Either<out E : DomainError, out S> {
    data class Success<out S>(val data: S) : Either<Nothing, S>
    data class Failure<out E : DomainError>(val error: E) : Either<E, Nothing>
}

interface DomainError

class EitherScope<E : DomainError, T> @PublishedApi internal constructor()

@PublishedApi
internal class ErrorThrowable(val error: DomainError) : Throwable()

inline fun <T, E : DomainError, R> Either<E, T>.map(
    map: (T) -> R,
): Either<E, R> = when (this) {
    is Either.Failure -> Either.Failure(error)
    is Either.Success -> Either.Success(map(data))
}

inline fun <T, E1 : DomainError, E2 : DomainError> Either<E1, T>.mapFailure(
    map: (E1) -> E2,
): Either<E2, T> = when (this) {
    is Either.Failure -> Either.Failure(map(error))
    is Either.Success -> Either.Success(data)
}

inline fun <T, E : DomainError> Either<E, T>.onSuccess(
    action: (T) -> Unit,
): Either<E, T> = when (this) {
    is Either.Success -> {
        action(this.data)
        this
    }
    is Either.Failure -> this
}

inline fun <T, E : DomainError> Either<E, T>.onFailure(
    action: (E) -> Unit,
): Either<E, T> = when (this) {
    is Either.Failure -> {
        action(error)
        this
    }

    is Either.Success -> this
}

inline fun <T, E : DomainError, R> Either<E, T>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R,
): R = when (this) {
    is Either.Success -> onSuccess(data)
    is Either.Failure -> onFailure(error)
}

fun <T, E : DomainError> Either<E, T>.getOrNull(): T? = when (this) {
    is Either.Success<T> -> data
    is Either.Failure<E> -> null
}

fun <T, E : DomainError> Either<E, T>.getOrElse(
    defaultValue: (E) -> T,
): T? = when (this) {
    is Either.Success<T> -> data
    is Either.Failure<E> -> defaultValue(error)
}

fun <E : DomainError, T> Either<E, T>.asEmptyEither(): EmptyEither<E> = map { Unit }

fun <E : DomainError, T> E.asFailure(): Either<E, T> = Either.Failure(this)
fun <E : DomainError, T> T.asSuccess(): Either<E, T> = Either.Success(this)

typealias EmptyEither<E> = Either<E, Unit>
typealias ErrorEither<E> = Either<E, Nothing>

// Experimental
@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError, T> either(
    body: context(EitherScope<E, T>) () -> T,
): Either<E, T> = try {
    body(EitherScope()).asSuccess()
} catch (t: ErrorThrowable) {
    Either.Failure(t.error as E)
}

inline fun <E : DomainError> emptyEither(body: context(EitherScope<E, Unit>) () -> Unit): EmptyEither<E> =
    either(body)

@Suppress("UNCHECKED_CAST")
inline fun <E : DomainError> errorResult(
    body: context(EitherScope<E, Nothing>) () -> Nothing,
): ErrorEither<E> = try {
    body(EitherScope())
} catch (t: ErrorThrowable) {
    Either.Failure(t.error as E)
}

context(_: EitherScope<E, T>)
fun <E : DomainError, T> E.raise(): Nothing = throw ErrorThrowable(this)

context(_: EitherScope<E, T>)
fun <E : DomainError, T> Either<E, T>.bind(): T = when (this) {
    is Either.Failure<E> -> error.raise()
    is Either.Success<T> -> data
}
