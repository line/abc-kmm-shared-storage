plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")
}

val sharedStorageVersion: String by rootProject

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("com.linecorp.abc.kmm-shared-storage-annotations:$sharedStorageVersion")
    ksp("com.linecorp.abc:kmm-shared-storage-annotations:$sharedStorageVersion")
}

android {
    sourceSets["main"].java {
        srcDir("${buildDir.absolutePath}/generated/ksp/debug")
    }
    compileSdk = 30
    defaultConfig {
        applicationId = "androidApp.sample"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}