plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

android {
    namespace = "com.georgian.moviesearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.georgian.moviesearchapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/io.netty.versions.properties")
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    // Core dependencies
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")

    // Compose dependencies
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Ktor Client dependencies
    implementation("io.ktor:ktor-client-core:2.3.0") // Core Ktor client
    implementation("io.ktor:ktor-client-android:2.3.0") // Android support for Ktor
    implementation("io.ktor:ktor-client-serialization:2.3.0") // Serialization support
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0") // Content negotiation support
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Kotlinx serialization

    // Ktor serialization support
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Dagger Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // Hilt Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Testing dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.8")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.8")
}