plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.portes.ufctracker.core.domain"
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

    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + listOf(
        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    )
}

dependencies {

    implementation("androidx.core:core-ktx:${Versions.AndroidX.core}")

    implementation("com.google.dagger:hilt-android:${Versions.Google.Hilt.android}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.Google.Hilt.compiler}")

    implementation("com.jakewharton.timber:timber:${Versions.Other.timber}")

    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}
