@file:Suppress("UnstableApiUsage")


import org.jetbrains.dokka.DokkaConfiguration
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.dokka")
    alias(libs.plugins.compose.compiler)
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

    implementation("com.github.miroslavhybler:jet-lint:1.0.2")
    //  implementation("com.github.miroslavhybler:jet-utils:1.0.2")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    /** Compose */
    val composeVersion = "1.6.8"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material:material:$composeVersion")
    val materialVersion = "1.2.1"
    implementation("androidx.compose.material3:material3:$materialVersion")
    implementation("androidx.compose.material3:material3-window-size-class:${materialVersion}")
    implementation("androidx.compose.animation:animation-graphics:1.6.8")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = "mir.oslav.jet"
                artifactId = "animated-icons"
                version = "1.0.0-alpha10"
                pom {
                    description.set("Jitpack.io deploy")
                }
            }

        }
        repositories {
            mavenLocal()
        }
    }
}