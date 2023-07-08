plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.portes.ufctracker.feature.events"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:${Versions.AndroidX.core}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecyle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecyleViewmodel}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.AndroidX.coroutines}")

    implementation("com.google.dagger:hilt-android:${Versions.Google.Hilt.android}")
    implementation("androidx.hilt:hilt-navigation-compose:${Versions.Google.Hilt.navigationCompose}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.Google.Hilt.compiler}")

    implementation("androidx.compose.ui:ui:${Versions.Compose.ui}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.Compose.uiPreview}")
    implementation("androidx.compose.material:material:${Versions.Compose.material}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${Versions.Compose.runtimeCompose}")
    implementation("io.coil-kt:coil-compose:${Versions.Compose.coil}")

    implementation("com.jakewharton.timber:timber:${Versions.Other.timber}")

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
}