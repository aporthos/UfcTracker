plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:${Versions.AndroidX.core}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecyle}")

    implementation("androidx.activity:activity-compose:${Versions.Compose.activity}")
    implementation("androidx.compose.ui:ui:${Versions.Compose.ui}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.Compose.uiPreview}")
    implementation("androidx.compose.material:material:${Versions.Compose.material}")
    implementation("androidx.navigation:navigation-compose:${Versions.Compose.navigation}")

    implementation("com.google.dagger:hilt-android:${Versions.Google.Hilt.android}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.Google.Hilt.compiler}")
    implementation("com.jakewharton.timber:timber:${Versions.Other.timber}")

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:database"))

    implementation(project(":feature:events"))
    implementation(project(":feature:fightbets"))
}

kapt {
    correctErrorTypes = true
}
