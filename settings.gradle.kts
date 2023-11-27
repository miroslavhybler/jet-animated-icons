@file:Suppress("UnstableApiUsage")

include(":benchmark")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.github.com/miroslavhybler/Maven")
    }
}

rootProject.name = "Jet Animated Icons"
include(":jet-animated-icons")
include(":example-app")