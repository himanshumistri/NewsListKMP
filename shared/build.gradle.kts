@file:Suppress("DSL_SCOPE_VIOLATION")

import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.gmazzo.buildconfig)
    //id("com.github.gmazzo.buildconfig") version "<current version>"
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTarget.get()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    )
        .takeIf { "XCODE_VERSION_MAJOR" in System.getenv().keys } // Export the framework only for Xcode builds
        ?.forEach {
            // This `shared` framework is exported for app-ios-swift
            it.binaries.framework {
                baseName = "shared" // Used in app-ios-swift

                export(libs.decompose.decompose)
                export(libs.essenty.lifecycle)
            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose Libraries
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)

                api(libs.decompose.decompose)
                api(libs.essenty.lifecycle)
                api(libs.kmm.viewmodel.core)

                api(libs.koin.core)
                api(libs.koin.test)
                api(libs.ktor.client.core)
                api(libs.ktor.serialization.kotlinx.json)
                api(libs.ktor.client.logging)
                api(libs.ktor.client.content.negotiation)
                api(libs.kotlinx.coroutines.core)
                api(libs.kermit)

            }
        }

        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.example.myapplication.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.register("printApiKey")  {
    group = "Security"
    description = "Prints the value of the API_KEY property from secrets.properties (if available)."

    doFirst {
        val apiKey =  project.properties["API_KEY"] as? String
        if (apiKey != null) {
            println("API_KEY (for informational purposes only): $apiKey")
            logger.warn("Storing API keys in project properties is not ideal. Consider using environment variables for secure storage.")
        } else {
            println("API_KEY property not found in secrets.properties.")
        }
    }
}


tasks.register("printApiKeyFile") {
    group = "Security"
    description = "Prints the value of the API_KEY property from secrets.properties (if available)."

    doFirst {
        val path = "${project.projectDir.absolutePath}/secrets.properties"
        println("File path is $path")
        val propertiesFile = File(path) // Replace with your file path
        if (propertiesFile.exists()) {
            val props = Properties()
            try {
                props.load(propertiesFile.inputStream())
                val apiKey = props.getProperty("API_KEY")
                if (apiKey != null) {
                    println("API_KEY (for informational purposes only): $apiKey")
                    logger.warn("Storing API keys in project properties is not ideal. Consider using environment variables for secure storage.")
                } else {
                    println("API_KEY property not found in secrets.properties.")
                }
            } catch (e: Exception) {
                logger.warn("Error loading secrets.properties: $e")
            }
        } else {
            println("secrets.properties file not found.")
        }
    }
}

buildConfig {
    val path = "${project.projectDir.absolutePath}/secrets.properties"
    var mApiKey =""
    println("File path is $path")
    val propertiesFile = File(path) // Replace with your file path
    if (propertiesFile.exists()) {
        val props = Properties()
        try {
            props.load(propertiesFile.inputStream())
            val apiKey = props.getProperty("API_KEY")
            if (apiKey != null) {
                println("API_KEY (for informational purposes only): $apiKey")
                mApiKey = apiKey
                logger.warn("Storing API keys in project properties is not ideal. Consider using environment variables for secure storage.")
            } else {
                println("API_KEY property not found in secrets.properties.")
            }
        } catch (e: Exception) {
            logger.warn("Error loading secrets.properties: $e")
        }
    } else {
        println("secrets.properties file not found.")
    }
    buildConfigField("API_KEY",mApiKey)
}
