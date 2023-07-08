import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.portes.ufctracker.core.data"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("boolean", "READ_LOCAL", "true")
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

    implementation("com.squareup.moshi:moshi:${Versions.Squareup.Moshi.moshi}")
    implementation("com.jakewharton.timber:timber:${Versions.Other.timber}")

    implementation(project(":core:common"))
    implementation(project(":core:model"))
}