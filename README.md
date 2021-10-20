# shared-storage-kmm

[![Kotlin](https://img.shields.io/badge/kotlin-1.5.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![KMM](https://img.shields.io/badge/KMM-0.2.7-lightgreen.svg?logo=KMM)](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
[![AGP](https://img.shields.io/badge/AGP-7.0.1-green.svg?logo=AGP)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-blue.svg?logo=Gradle)](https://gradle.org)
[![Platform](https://img.shields.io/badge/platform-ios,android-lightgray.svg?style=flat)](https://img.shields.io/badge/platform-ios-lightgray.svg?style=flat)

A local storage management library for Kotlin Multiplatform Mobile iOS and android

## Features

- iOS and Android local storage in one interface
- Provides common storage (UserDefaults, SharedPreferences)
- Provides secure storage (Keychain, EncryptedPreferences)
- Annotation is provided to create a custom class only by defining an interface
- Common interface available on KMM Shared

## Requirements

- iOS 
    - Deployment target 10.0 or higher
- Android
    - minSdkVersion 21

## Installation

### Default Gradle Settings

Add below gradle settings into your KMP (Kotlin Multiplatform Project)

#### build.gradle.kts in root

```kotlin
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.21")
        classpath("com.google.gms:google-services:4.3.5")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("http://repo.navercorp.com/maven-release/")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("http://repo.navercorp.com/maven-snapshot/")
            isAllowInsecureProtocol = true
        }
    }
}
```

#### build.gradle.kts in shared
```kotlin
plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

val sharedStorageVersion = "1.0.1"
val sharedStorage = "com.linecorp.abc-kmm:shared-storage-annotations:$sharedStorageVersion"

kotlin {
    sourceSets {
        ios {
            binaries
                .filterIsInstance<Framework>()
                .forEach {
                    it.transitiveExport = true
                    it.export(sharedStorage)
                }
        }
        android()

        val commonMain by getting {
            dependencies {
                implementation(sharedStorage)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(sharedStorage)
                api(sharedStorage)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(sharedStorage)
                api(sharedStorage)
            }
        }
    }
}
```

## Usage

### To store general value

Android
```kotlin
SharedStorage.save(100, "key::Int")
SharedStorage.save(100f, "key::Float")
SharedStorage.save(102L, "key::Long")
SharedStorage.save(true, "key::Boolean")
SharedStorage.save("String", "key::String")
```

iOS
```swift
SharedStorage.Companion().save(value: 100, forKey: "key::Int")
SharedStorage.Companion().save(value: 100.0, forKey: "key::Float")
SharedStorage.Companion().save(value: 102.0, forKey: "key::Long")
SharedStorage.Companion().save(value: true, forKey: "key::Boolean")
SharedStorage.Companion().save(value: "String", forKey: "key::String")
```

### To store secure value

Android
```kotlin
SharedStorage.secureSave("SecureString", "key::SecureString")
```

iOS
```swift
SharedStorage.Companion().secureSave(value: "SecureString", forKey: "key::SecureString")
```

### To get general value

Android
```kotlin
SharedStorage.load("key::Int", 0)
SharedStorage.load("key::Float", 0f)
SharedStorage.load("key::Long", 0L)
SharedStorage.load("key::Boolean", false)
SharedStorage.load("key::String", "")
```

iOS
```swift
SharedStorage.Companion().load(key: "key::Int", default: 0)
SharedStorage.Companion().load(key: "key::Float", default: 0.0)
SharedStorage.Companion().load(key: "key::Long", default: 0.0)
SharedStorage.Companion().load(key: "key::Boolean", default: "")
SharedStorage.Companion().load(key: "key::String", default: "")
```

### To store secure value

Android
```kotlin
SharedStorage.secureSave("SecureString", "key::SecureString")
```

iOS
```swift
SharedStorage.Companion().secureSave(value: "SecureString", forKey: "key::SecureString")
```

### To get secure value

Android
```kotlin
SharedStorage.secureLoad("key::SecureString", "")
```

iOS
```swift
SharedStorage.Companion().secureLoad(key: "key::SecureString", default: "")
```

## Advanced

### To Use Annotations for Code Generating (Android only)

#### settings.gradle.kts in project root
```kotlin
val kotlinVersion = "1.5.21"
val kspVersion = "1.5.21-1.0.0-beta07"

plugins {
    kotlin("jvm") version kotlinVersion
    id("com.google.devtools.ksp") version kspVersion
}
```

#### build.gradle.kts in androidApp
```kotlin
val sharedStorageVersion = "1.0.1"

dependencies {
    implementation(project(":shared"))
    implementation("com.linecorp.abc-kmm:shared-storage-annotations:$sharedStorageVersion")
    ksp("com.linecorp.abc-kmm:shared-storage-annotations:$sharedStorageVersion")
}
```

#### Define your interface for code generating
```kotlin
package androidApp.sample

import com.linecorp.abc.sharedstorage.annotations.Secure
import com.linecorp.abc.sharedstorage.annotations.SharedStorage

@SharedStorage
interface AppData {
    var someInt: Int
    var someFloat: Float
    val someLong: Long
    val someDouble: Double
    val someBoolean: Boolean
    val someString: String

    @Secure val someSecureString: String
}
```

#### Using for your android project
```kotlin
SharedAppData.someInt = 501
SharedAppData.someFloat = 501.5f
SharedAppData.someLong = 500500L
SharedAppData.someBoolean = true
SharedAppData.someString = "I'm Some String"
SharedAppData.someSecureString = "I'm Encrypted String"
```

### Integration with @propertyWrapper on iOS

```swift
struct AppData {

    @General(key: "SomeInt", default: 0)
    static var someInt: Int

    @General(key: "SomeFloat", default: 0)
    static var someFloat: Float

    @General(key: "SomeDouble", default: 0)
    static var someDouble: Double

    @General(key: "SomeBool", default: false)
    static var someBool: Bool

    @General(key: "SomeString", default: "")
    static var someString: String

    @Secure(key: "SomeSecure", default: "")
    static var secureString: String
}

@propertyWrapper
struct General<Value> {
    let key: String
    let `default`: Value

    var wrappedValue: Value {
        get { SharedStorage.Companion().load(key: key, default: `default`) as? Value ?? `default` }
        set { SharedStorage.Companion().save(value: newValue, forKey: key) }
    }
}

@propertyWrapper
struct Secure {
    let key: String
    let `default`: String

    var wrappedValue: String {
        get { SharedStorage.Companion().secureLoad(key: key, default: `default`) }
        set { SharedStorage.Companion().secureSave(value: newValue, forKey: key) }
    }
}
```

## Maven Publish
### local
```bash
./gradlew publishToMavenLocal -Dmaven.local=true
```

### remote
```bash
./gradlew publish -Dmaven.username=${username} -Dmaven.password=${password}
```
