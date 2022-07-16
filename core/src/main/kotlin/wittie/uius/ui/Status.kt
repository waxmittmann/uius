package wittie.uius.ui

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some

data class Error(val msg: String)

typealias Status = Option<Error>
val OkStatus = None
fun error(msg: String) = Some(Error(msg))

typealias StatusOr<S> = Either<Error, S>
fun <S> orError(msg: String): Either<Error, S> = Either.Left(Error(msg))
fun <S> orOk(value: S): Either<Error, S> = Either.Right(value)
