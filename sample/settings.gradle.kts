pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
    }

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name = "sample"

include(":shared")
include(":androidApp")
include(":iosApp")