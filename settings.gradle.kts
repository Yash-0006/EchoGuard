pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Prioritize repositories in settings.gradle.kts
    repositories {
        google()         // Required for Android dependencies
        mavenCentral()   // For other Maven dependencies
    }
}

rootProject.name = "EchoGuard"
include(":app")
