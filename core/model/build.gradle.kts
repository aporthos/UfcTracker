plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.portes.ufctracker.core.model"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

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
    implementation("com.squareup.moshi:moshi:1.11.0")
    implementation(project(":core:database"))
    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation(project(":core:common"))
}
