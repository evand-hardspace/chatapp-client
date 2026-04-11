package com.evandhardspace.core.presentation.util.datetime

import chatapp.client.core.presentation.generated.resources.Res
import chatapp.client.core.presentation.generated.resources.today
import chatapp.client.core.presentation.generated.resources.yesterday
import chatapp.client.core.presentation.generated.resources.today_time
import chatapp.client.core.presentation.generated.resources.yesterday_time
import com.evandhardspace.core.presentation.util.UiText
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun Instant.formatMessageTime(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): UiText {
    val messageDateTime = toLocalDateTime(timeZone)
    val todayDate = clock.now().toLocalDateTime(timeZone).date
    val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

    val formattedTime = messageDateTime.format(
        format = LocalDateTime.Format {
            amPmHour()
            char(':')
            minute()
            amPmMarker("am", "pm")
        }
    )
    val formattedDateTime = messageDateTime.format(
        LocalDateTime.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
            chars(", $formattedTime")
        }
    )

    return when (messageDateTime.date) {
        todayDate -> Res.string.today_time.asUiText(formattedTime)
        yesterdayDate -> Res.string.yesterday_time.asUiText(formattedTime)
        else -> formattedDateTime.asUiText()
    }
}

fun LocalDate.formatDateSeparator(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): UiText {
    val date = this
    val today = clock.now().toLocalDateTime(timeZone).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return when (date) {
        today -> Res.string.today.asUiText()
        yesterday -> Res.string.yesterday.asUiText()
        else -> {
            val formatted = date.format(
                LocalDate.Format {
                    day()
                    char('/')
                    monthNumber()
                    char('/')
                    year()
                }
            )
            formatted.asUiText()
        }
    }
}

fun LocalDate.isToday(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean = this == clock.now().toLocalDateTime(timeZone).date
