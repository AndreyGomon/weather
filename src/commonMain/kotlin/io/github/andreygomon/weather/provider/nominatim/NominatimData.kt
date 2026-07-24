package io.github.andreygomon.weather.provider.nominatim

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class NominatimRequests(val maxRequests: Long) {
    PER_MINUTE(60),
    PER_HOUR(3600),
    PER_DAY(86400);

    companion object {
        fun fromString(period: String): NominatimRequests {
            return when (period.lowercase()) {
                "minute" -> PER_MINUTE
                "hour" -> PER_HOUR
                "day" -> PER_DAY
                else -> throw IllegalArgumentException("Unknown period: $period")
            }
        }
    }
}

@Serializable
data class NominatimResponse(
    @SerialName("place_id") val placeId: Long,
    @SerialName("lat") val lat: String,
    @SerialName("lon") val lon: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("address") val address: NominatimAddress
)

@Serializable
data class NominatimAddress(
    @SerialName("village") val village: String? = null,
    @SerialName("town") val town: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("country") val country: String? = null
) {
    val name: String
        get() = city ?: town ?: village ?: state ?: country ?: "Не удалось определить"
}