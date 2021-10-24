import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

val sharedStorageVersion: String by rootProject
val sharedStorageLib = "com.linecorp.abc:kmm-shared-storage:$sharedStorageVersion"

version = "1.0"

kotlin {
    ios {
        binaries
            .filterIsInstance<Framework>()
            .forEach {
                it.transitiveExport = true
                it.export(sharedStorageLib)
            }
    }
    android()

    cocoapods {
        ios.deploymentTarget = "10.0"
        homepage = "https://github.com/line/abc-kmm-shared-storage/sample/iosApp"
        summary = "Sample for abc-kmm-shared-storage"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(sharedStorageLib)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                api(sharedStorageLib)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("androidx.test:core:1.0.0")
                implementation("androidx.test:runner:1.1.0")
                implementation("org.robolectric:robolectric:4.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(sharedStorageLib)
                api(sharedStorageLib)
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir("src/androidMain/res")
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}