package com.evandhardspace.core.presentation.util.datetime

import chatapp.core.presentation.generated.resources.Res
import chatapp.core.presentation.generated.resources.today
import chatapp.core.presentation.generated.resources.yesterday
import com.evandhardspace.core.presentation.util.UiText
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun Instant.formatMessageTime(
    clock: Clock = Clock.System, // TODO: Inject
    timeZone: TimeZone = TimeZone.currentSystemDefault(), // TODO: Inject
): UiText {
    val messageDateTime = toLocalDateTime(timeZone)
    val todayDate = clock.now().toLocalDateTime(timeZone).date
    val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

    val formattedDateTime = messageDateTime.format(
        LocalDateTime.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
            char(' ')
            amPmHour()
            char(':')
            minute()
            amPmMarker("am", "pm")
        }
    )

    return when (messageDateTime.date) {
        todayDate -> Res.string.today.asUiText()
        yesterdayDate -> Res.string.yesterday.asUiText()
        else -> formattedDateTime.asUiText()
    }
}
