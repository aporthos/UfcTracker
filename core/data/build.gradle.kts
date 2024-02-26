plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
//    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.portes.ufctracker.core.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

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
    implementation("androidx.core:core-ktx:1.10.1")

    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("com.squareup.moshi:moshi:1.11.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
}
