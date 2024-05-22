@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.jetbrains.compose).apply(false)
    //id("com.github.gmazzo.buildconfig") version "5.3.5"
    alias(libs.plugins.gmazzo.buildconfig).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
