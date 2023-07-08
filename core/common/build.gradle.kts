plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("org.jmailen.kotlinter")
}

android {
    namespace = "com.portes.ufctracker.core.common"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:${Versions.AndroidX.core}")

    implementation("com.google.dagger:hilt-android:${Versions.Google.Hilt.android}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.Google.Hilt.compiler}")

    implementation("com.squareup.retrofit2:retrofit:${Versions.Squareup.Retrofit.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.Squareup.Retrofit.converter}")

    implementation("com.squareup.okhttp3:okhttp:${Versions.Squareup.Okhttp3.okhttp3}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.Squareup.Okhttp3.interceptor}")

    implementation("com.squareup.moshi:moshi:${Versions.Squareup.Moshi.moshi}")
    implementation("com.squareup.moshi:moshi-kotlin:${Versions.Squareup.Moshi.kotlin}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.Squareup.Moshi.codegen}")
}