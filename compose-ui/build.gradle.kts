@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.serialization)
   // alias(libs.plugins.compose.compiler)
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

        /*@OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }*/
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )
        .takeIf { "XCODE_VERSION_MAJOR" in System.getenv().keys } // Export the framework only for Xcode builds
        ?.forEach {
            // This `shared` framework is exported for app-ios-compose
            it.binaries.framework {
                baseName = "shared" // Used in app-ios-compose
                isStatic= true
                export(project(":shared"))
                export(libs.decompose.decompose)
            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))

                // Compose Libraries
                api(compose.ui)
                api(compose.foundation)
                api(compose.material)

                // Decompose Libraries
                api(libs.decompose.decompose)
                api(libs.decompose.extensionsComposeJetbrains)
                api(libs.landscapist.coil3)
            }
        }
    }

    task("testClasses")
}

android {
    namespace = "com.example.myapplication.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
