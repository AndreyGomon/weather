import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.sqldelight)
    id("maven-publish")
}

group = "io.github.andreygomon"
version = "1.0.0"

kotlin {
    withSourcesJar(publish = false)
    jvmToolchain(17)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release")
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)

            api(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            api(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)

            implementation(libs.compottie)

            api(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
        }


        val desktopMain by getting {
            kotlin.srcDir("src/desktopMain/kotlin")
        }

        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}

android {
    namespace = "io.github.andreygomon.weather"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("WeatherDatabase") {
            packageName.set("io.github.andreygomon.weather")
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.andreygomon.weather.resources"
    generateResClass = always
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"

            url = uri(
                "https://maven.pkg.github.com/andreygomon/weather"
            )

            credentials {
                username = providers
                    .gradleProperty("gpr.user")
                    .orElse(
                        providers.environmentVariable("GITHUB_ACTOR")
                    )
                    .orNull

                password = providers
                    .gradleProperty("gpr.key")
                    .orElse(
                        providers.environmentVariable("GITHUB_TOKEN")
                    )
                    .orNull
            }
        }
    }
}
