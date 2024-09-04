plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.echoguard"
    compileSdk = 34
    // Use the latest SDK version available
    defaultConfig {
        applicationId = "com.example.echoguard"
        minSdk = 21
        targetSdk = 33
        // Use the latest SDK version available
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources.excludes.add("javax/xml/bind/util/Messages.properties")
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/DEPENDENCIES.txt")
        resources.excludes.add("META-INF/NOTICE.md")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
    }
}

dependencies {
    implementation(libs.play.services.location)
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.appcompat.v162)
    implementation(libs.androidx.constraintlayout)
    implementation("com.twilio.sdk:twilio:8.29.0") {
        exclude(group= "javax.xml.bind", module= "jaxb-api")
        exclude(group="javax.activation", module="javax.activation-api")
    }

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))

    // Android Testing
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
}
