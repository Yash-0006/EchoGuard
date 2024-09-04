buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // If needed
    }
    dependencies {
        classpath(libs.gradle.v860)
        classpath(libs.kotlin.gradle.plugin)
    }
}

