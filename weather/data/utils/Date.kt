package ru.mascot.features.weather.data.utils

import kotlinx.datetime.*
import java.util.Locale

object DateDay {
    private val ruDays = mapOf(
        DayOfWeek.MONDAY to ("Понедельник" to "Пн"),
        DayOfWeek.TUESDAY to ("Вторник" to "Вт"),
        DayOfWeek.WEDNESDAY to ("Среда" to "Ср"),
        DayOfWeek.THURSDAY to ("Четверг" to "Чт"),
        DayOfWeek.FRIDAY to ("Пятница" to "Пт"),
        DayOfWeek.SATURDAY to ("Суббота" to "Сб"),
        DayOfWeek.SUNDAY to ("Воскресенье" to "Вс")
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
        "декабря" to "дек"
    )

    fun get(day: DayOfWeek, short: Boolean): String = ruDays[day]?.let { if (short) it.second else it.first } ?: "?"

    fun formatHour(timestamp: Long, zone: TimeZone = TimeZone.currentSystemDefault()): String {
        val dateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone)
        return String.format(Locale.getDefault(), "%02d:%02d", dateTime.hour, dateTime.minute)
    }

    fun formatDayWeek(timestamp: Long, zone: TimeZone = TimeZone.currentSystemDefault(), short: Boolean): String {
        val dateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone)
        return get(dateTime.date.dayOfWeek, short)
    }

    fun formatDateMonth(timestamp: Long, zone: TimeZone = TimeZone.currentSystemDefault(), short: Boolean): String {
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(zone).date
        val day = date.dayOfMonth
        val month = if (short) ruMonths[date.monthNumber - 1].second else ruMonths[date.monthNumber - 1].first
        return "$day $month"
    }
}