pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                val agpVersion: String by settings
                useModule("com.android.tools.build:gradle:$agpVersion")
            }
        }
    }
}

rootProject.name = "abc-kmm-shared-storage"
include(":shared-storage")
include(":shared-storage-annotations")