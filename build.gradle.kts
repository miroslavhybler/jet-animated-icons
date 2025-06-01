plugins {
    `kotlin-dsl`
    id("com.android.application") version "8.10.1" apply false
    id("com.android.library") version "8.10.1" apply false
    id("com.android.test") version "8.10.1" apply false

    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("org.jetbrains.dokka") version "2.0.0" apply false
    alias(libs.plugins.compose.compiler) apply false
}

java {
    withSourcesJar()
    withJavadocJar()
}