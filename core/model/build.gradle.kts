plugins {
    id("kotlin")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implementation("com.squareup.moshi:moshi:${Versions.Squareup.Moshi.moshi}")
}
