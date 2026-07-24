# Weather

Standalone Kotlin Multiplatform weather library for Android and Desktop/JVM.

## Coordinates

- Maven group: `io.github.andreygomon`
- Artifact: `weather`
- Kotlin package: `io.github.andreygomon.weather`
- Android namespace: `io.github.andreygomon.weather`
- Development version: `0.1.0-SNAPSHOT`

The library does not depend on a map framework. The host application passes plain coordinates:

```kotlin
val point = Coordinates(
    latitude = 55.7558,
    longitude = 37.6173,
)
```

## Database ownership

The host owns and configures the SQLDelight `SqlDriver`. This is intentional: the application may use a regular, encrypted, in-memory, Android, or JDBC driver without the weather library choosing one for it.

### Android

```kotlin
val driver = AndroidSqliteDriver(
    schema = WeatherDatabase.Schema,
    context = applicationContext,
    name = "weather.db",
)
```

### Desktop/JVM

```kotlin
val driver = JdbcSqliteDriver("jdbc:sqlite:weather.db")
WeatherDatabase.Schema.create(driver)
```

For an existing database, apply migrations instead of calling `Schema.create` again.

## Koin integration

```kotlin
startKoin {
    modules(weatherModule(driver))
}
```

The ready-made module provides `WeatherApi`, `WeatherService`, and the Compose ViewModels.

Without Koin:

```kotlin
val weatherApi = createWeatherApi(driver)
```

## Local Maven publication

```bash
./gradlew clean build
./gradlew publishToMavenLocal
```

Consumer dependency:

```kotlin
commonMain.dependencies {
    implementation("io.github.andreygomon:weather:0.1.0-SNAPSHOT")
}
```

## Developing together with `mmb`

Add the repository as a Git submodule from the root of `mmb`:

```bash
git submodule add https://github.com/AndreyGomon/weather.git weather
git submodule update --init --recursive
```

Then add the standalone build in `mmb/settings.gradle.kts`:

```kotlin
includeBuild("weather") {
    dependencySubstitution {
        substitute(module("io.github.andreygomon:weather"))
            .using(project(":"))
    }
}
```

Do not add `include(":weather")`: this repository remains an independent build and is consumed as a library.

## Package display in Android Studio

The physical source path must remain:

```text
src/commonMain/kotlin/io/github/andreygomon/weather/...
```

Seeing `io / github / andreygomon / weather` as separate directories is only a Project tool-window display mode. Select the **Packages** view or enable **Compact Middle Packages** in the tool-window options to display `io.github.andreygomon.weather` as one compact node.
