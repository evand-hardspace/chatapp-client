package com.evandhardspace.chat.data.database

import androidx.sqlite.SQLiteException
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.asFailure
import com.evandhardspace.core.domain.util.asSuccess

suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): Either<DataError.Local, T> =
    try {
        update().asSuccess()
    } catch (_: SQLiteException) {
        DataError.Local.DiskFull.asFailure()
    }
