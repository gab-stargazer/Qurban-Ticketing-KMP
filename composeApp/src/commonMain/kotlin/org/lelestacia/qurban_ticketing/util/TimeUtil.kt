package org.lelestacia.qurban_ticketing.util

import androidx.compose.material3.SelectableDates
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@OptIn(ExperimentalTime::class)
fun Long.toFormattedDate(): String {
    val instant = Instant.fromEpochMilliseconds(this).toJavaInstant()

    val zonedDateTime: ZonedDateTime =
        ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formattedDate: String =
        zonedDateTime.format(
            DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                Locale.forLanguageTag("id")
            )
        )

    return formattedDate
}

@OptIn(ExperimentalTime::class)
object FutureSelectableDate : SelectableDates {

    private val zonedDateTime = ZonedDateTime.ofInstant(
        Clock.System.now().toJavaInstant(),
        ZoneId.systemDefault()
    )

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis > Clock.System.now().toEpochMilliseconds()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= zonedDateTime.year
    }
}