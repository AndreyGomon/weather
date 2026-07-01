package ru.mascot.features.weather.service

import dev.jordond.compass.geolocation.TrackingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ru.mascot.features.objects.domain.repository.MapFolderRepository
import ru.mascot.features.objects.domain.repository.MapObjectRepository
import ru.mascot.features.weather.api.WeatherApi
import ru.mascot.features.weather.api.WeatherApiImpl
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.data.WeatherSettings
import ru.mascot.features.weather.data.WeatherStat
import ru.mascot.features.weather.plugin.WeatherType
import ru.mascot.features.weather.provider.LocationProviderFactory
import ru.mascot.features.weather.provider.WeatherProviderFactory
import ru.mascot.features.weather.provider.nomenatim.NominatimProvider
import ru.onyx.map.geolocation.domain.MultiGeolocator
import java.util.Timer

@Single(binds = [WeatherService::class])
class WeatherService(
    private val weatherAPI: WeatherApi,
    val mapFoldersRepo: MapFolderRepository,
    val mapObjectsRepo: MapObjectRepository
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var location: GeoPoint? = null
    private var timer: Timer? = null

    val stats: StateFlow<List<WeatherStat>> get() = weatherAPI.stats
    val settings: StateFlow<WeatherSettings> get() = weatherAPI.settings
    val currentWeather: StateFlow<WeatherData> get() = weatherAPI.currentWeather
    val currentForecast: StateFlow<ForecastList> get() = weatherAPI.currentForecast
    val currentHourly: StateFlow<HourlyList> get() = weatherAPI.currentHourly

    private var weatherProvider = settings.map {
        WeatherProviderFactory.createProvider(it.provider)
    }.stateIn(
        scope,
        SharingStarted.Eagerly,
        initialValue = null
    )

    private var locationProvider = LocationProviderFactory.createProvider(NominatimProvider().name)

    init {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        var job: Job? = null

        // Получить все объекты своего типа
        mapObjectsRepo.current.value.filter { it.payload.type is WeatherType }

        val locationManager = getKoin().get<MultiGeolocator>()

//        locationManager.currentLocation.value?.let { location ->
//            _scope.launch { fetchWeather(GeoPoint(location.latitude, location.longitude)) }
//        }

        val locationFlow = locationManager.trackingStatus.map {
            when (it) {
                is TrackingStatus.Update -> {
                    GeoPoint(
                        it.location.coordinates.latitude,
                        it.location.coordinates.longitude
                    )
                }

                else -> null
            }
        }

        locationFlow
            .onEach { location = it }
            .launchIn(scope)

        settings.map { it.source == WeatherProviderFactory.LocationSource.LOCATION.displayName }
            .onEach {}//updateTimer(it) }
            .launchIn(scope)

        combine(
            locationFlow,                 // GeoPoint?
            settings,                     // WeatherSettings
        ) { loc, s -> loc to s }
            .onEach { (loc, s) ->
                job?.cancel()
                job = null

                if (loc == null) return@onEach
                if (s.source != WeatherProviderFactory.LocationSource.LOCATION.displayName) return@onEach

                val providerName = s.provider

                job = ru.mascot.features.weather.utils.Timer(scope).launch(
                    key = "current-weather",
                    period = WeatherApiImpl.CURRENT_TTL.inWholeMilliseconds,
                    jitterMinMs = 1_000L,
                    jitterMaxMs = 60_000L,
                    runImmediately = true, // но фактически fetchWeather сам не будет дергать сеть если актуально
                ) {
                    weatherAPI.fetchWeather(loc, providerName, current = true)
                }
            }
            .launchIn(scope)
    }

//    private fun updateTimer(start: Boolean) {
//        weatherProvider.value?.let { provider ->
//            val fixedPeriod = FixedTimeoitType.getFixedMillis(FixedTimeoitType.QUAD.name)
//
//            if (start && timer == null) {
//                timer = timer(this::class.simpleName, true, 0L, fixedPeriod, action = {
//                    scope.launch {
//                        location?.let { weatherAPI.fetchWeather(it, provider.name, true) }
//                    }
//                })
//            } else if (!start && timer != null) {
//                timer?.cancel()
//                timer = null
//            }
//        }
//    }

}



