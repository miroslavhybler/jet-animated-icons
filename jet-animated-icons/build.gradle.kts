@file:Suppress("UnstableApiUsage")

import org.jetbrains.dokka.DokkaConfiguration
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

android {
    namespace = "mir.oslav.jet.icons"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    lint {
        disable += listOf(
            "RedundantVisibilityModifier",
            "unused"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kotlin {
        jvmToolchain(jdkVersion = 8)
    }
    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation("mir.oslav.jet:annotations:1.0.0")
    implementation("mir.oslav.jet:utils:1.0.2")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    /** Compose */
    val composeVersion = "1.5.4"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.compose.animation:animation-graphics:1.5.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve(relative = "dokkaHtml"))

    dokkaSourceSets {
        configureEach {
            pluginsMapConfiguration.set(
                mutableMapOf(
                    "org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}"""
                )
            )
            documentedVisibilities.set(
                mutableListOf(
                    DokkaConfiguration.Visibility.PUBLIC,
                    DokkaConfiguration.Visibility.PRIVATE,
                    DokkaConfiguration.Visibility.PROTECTED,
                    DokkaConfiguration.Visibility.INTERNAL,
                    DokkaConfiguration.Visibility.PACKAGE
                )
            )

            skipEmptyPackages.set(true)
            includeNonPublic.set(true)
            skipDeprecated.set(false)
            reportUndocumented.set(true)
            description = ""
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "mir.oslav.jet"
            artifactId = "animated-icons"
            version = "1.0.0-alpha05"
        }
    }
    repositories {
        mavenLocal()
    }
}
