package ru.mascot.features.weather.provider

interface LocationProvider: LocationRequests {
    val name: String
    val url: String
    suspend fun getLocation(latitude: Double, longitude: Double): String
}

interface LocationRequests {
    val perMinute: Long
    val perHour: Long
    val perDay: Long
    fun getRemaining(limit: Long, used: Long): Long {
        return (limit - used).coerceAtLeast(0)
    }
}