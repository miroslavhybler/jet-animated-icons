plugins {
    `kotlin-dsl`
    id("com.android.application") version "8.4.0" apply false
    id("com.android.library") version "8.4.0" apply false
    id("com.android.test") version "8.4.0" apply false

    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("org.jetbrains.dokka") version "1.9.20" apply false
}

java {
    withSourcesJar()
    withJavadocJar()
}