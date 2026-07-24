package io.github.andreygomon.weather.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

object DateDay {
    private val ruDays = mapOf(
        DayOfWeek.MONDAY to ("Понедельник" to "Пн"),
        DayOfWeek.TUESDAY to ("Вторник" to "Вт"),
        DayOfWeek.WEDNESDAY to ("Среда" to "Ср"),
        DayOfWeek.THURSDAY to ("Четверг" to "Чт"),
        DayOfWeek.FRIDAY to ("Пятница" to "Пт"),
        DayOfWeek.SATURDAY to ("Суббота" to "Сб"),
        DayOfWeek.SUNDAY to ("Воскресенье" to "Вс"),
    )

    private val ruMonths = listOf(
        "января" to "янв",
        "февраля" to "фев",
        "марта" to "мар",
        "апреля" to "апр",
        "мая" to "май",
        "июня" to "июн",
        "июля" to "июл",
        "августа" to "авг",
        "сентября" to "сен",
        "октября" to "окт",
        "ноября" to "ноя",
        "декабря" to "дек",
    )

    fun get(day: DayOfWeek, short: Boolean): String =
        ruDays[day]?.let { if (short) it.second else it.first } ?: "?"

    @OptIn(ExperimentalTime::class)
    fun formatHour(
        timestamp: Long,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        val dateTime = kotlin.time.Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone)
        return "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
    }

    @OptIn(ExperimentalTime::class)
    fun formatDayWeek(
        timestamp: Long,
        zone: TimeZone = TimeZone.currentSystemDefault(),
        short: Boolean,
    ): String {
        val dateTime = kotlin.time.Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone)
        return get(dateTime.date.dayOfWeek, short)
    }

    @OptIn(ExperimentalTime::class)
    fun formatDateMonth(
        timestamp: Long,
        zone: TimeZone = TimeZone.currentSystemDefault(),
        short: Boolean,
    ): String {
        val date = kotlin.time.Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone).date
        val month = if (short) {
            ruMonths[date.monthNumber - 1].second
        } else {
            ruMonths[date.monthNumber - 1].first
        }
        return "${date.dayOfMonth} $month"
    }
}
