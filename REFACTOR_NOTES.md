# Refactor notes

## Removed coupling to the former host project

- Replaced all `ru.mascot.*`, `ru.onyx.*`, map-compose, and `GeoPoint` references.
- Introduced the neutral `Coordinates` value type.
- Moved weather resources and vector icons into the library namespace.
- Removed host-only payload and protobuf references.
- Replaced JVM-only APIs from `commonMain` with multiplatform APIs.

## Data layer

- Retained SQLDelight for Android + Desktop/JVM compatibility.
- Added an internal `WeatherStorage` boundary so another persistence implementation can be added later without changing `WeatherApi`.
- The caller supplies the `SqlDriver`, allowing encrypted drivers owned by the host application.
- Cache keys now include coordinates and provider.
- Current weather, forecast, hourly data, settings, and provider request statistics are persisted independently.

## Dependency injection and UI

- Removed KSP-generated Koin modules.
- Added a normal Koin DSL module through `weatherModule(driver)`.
- ViewModels receive `WeatherApi` through constructors.
- The standalone screen no longer imports map or host-application UI components.

## Repository cleanup

- Removed `.idea`, `.gradle`, and the embedded `.git` directory from the distributable archive.
- Removed the accidental nested path `.../weather/src/commonMain/kotlin`.
- Added `.gitignore`.

## Build verification status

The project was statically audited in the isolated environment. A complete Gradle build could not be executed there because the Gradle 8.9 distribution was not cached and outbound downloading was unavailable. Run locally:

```bash
./gradlew clean build
./gradlew publishToMavenLocal
```
