plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // KSP (Kotlin Symbol Processing) - Used in Room for fast compilation
    id("com.google.devtools.ksp")

    // Hilt dependencies
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.movieappcleanarchitecture"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.movieappcleanarchitecture"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Accessing the TOKEN from the gradle.properties
        buildConfigField("String", "TMDB_TOKEN", "\"${project.findProperty("TMDB_TOKEN")}\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit Dependencies - Used for API calling
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // ViewModel for Compose and LiveData observation in Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime.livedata)

    // External Image Loading Library
    implementation(libs.coil.compose)

    // room database dependencies
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4") // Kotlin extensions and Coroutines support for Room
    ksp("androidx.room:room-compiler:2.8.4") // Kotlin Symbol Processing (KSP)

    // hilt dependencies
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-compiler:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}