import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
    kotlin("multiplatform")
}

kotlin {
    android {
        publishAllLibraryVariants()
    }

    val enableGranularSourceSetsMetadata = project.extra["kotlin.mpp.enableGranularSourceSetsMetadata"]?.toString()?.toBoolean() ?: false
    if (enableGranularSourceSetsMetadata) {
        val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64
        iosTarget("ios") {
            val main by compilations.getting {
                val keychain by cinterops.creating {
                    defFile(project.file("cinterop/keychain/keychain.def"))
                    includeDirs(project.file("cinterop/keychain"))
                }
            }
            main.enableEndorsedLibs = true
        }
    } else {
        ios {
            val main by compilations.getting {
                val keychain by cinterops.creating {
                    defFile(project.file("cinterop/keychain/keychain.def"))
                    includeDirs(project.file("cinterop/keychain"))
                }
            }
            main.enableEndorsedLibs = true
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.startup:startup-runtime:1.0.0")
                implementation("androidx.work:work-runtime:2.4.0")
                implementation("androidx.security:security-crypto:1.1.0-alpha01")
                implementation("com.scottyab:secure-preferences-lib:0.1.7")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
                implementation("androidx.test:core:1.0.0")
                implementation("androidx.test:runner:1.1.0")
                implementation("org.mockito.kotlin:mockito-kotlin:2.2.10")
                implementation("org.robolectric:robolectric:4.5.1")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    val compileSdkVersion = rootProject.ext.get("compileSdkVersion") as Int
    val minSdkVersion = rootProject.ext.get("minSdkVersion") as Int
    val targetSdkVersion = rootProject.ext.get("targetSdkVersion") as Int

    compileSdk = compileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("kmmSharedStorage") {
            artifactId = "kmm-shared-storage"
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    println("signingKey, signingPassword -> ${signingKey?.slice(0..9)}, ${signingPassword?.map { "*" }?.joinToString("")}")

    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}