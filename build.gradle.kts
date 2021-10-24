
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    val kotlinVersion: String by project
    val agpVersion: String by project
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$agpVersion")
    }
}

allprojects {
    ext {
        set("compileSdkVersion", 30)
        set("minSdkVersion", 21)
        set("targetSdkVersion", 30)
    }

    repositories {
        google()
        mavenCentral()
    }
}

group = "com.linecorp.abc"
version = "1.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        google()
    }

    pluginManager.withPlugin("maven-publish") {
        val isSnapshotUpload = false
        val publishExtension = extensions.getByType<PublishingExtension>()
        publishExtension.repositories {
            maven {
                url = if (isSnapshotUpload) {
                    uri("https://oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                }

                val sonatypeUsername: String? by project
                val sonatypePassword: String? by project

                println("sonatypeUsername, sonatypePassword -> $sonatypeUsername, ${sonatypePassword?.map { "*" }?.joinToString("")}")

                credentials {
                    username = sonatypeUsername ?: ""
                    password = sonatypePassword ?: ""
                }
            }
        }

        publishExtension.publications.whenObjectAdded {
            check(this is MavenPublication) {
                "unexpected publication $this"
            }

            groupId = rootProject.group.toString()
            version = if (isSnapshotUpload) "${rootProject.version}-SNAPSHOT" else rootProject.version.toString()

            pom {
                name.set(artifactId)
                description.set("A local storage management library for Kotlin Multiplatform Mobile iOS and android")
                url.set("https://github.com/line/${rootProject.name}")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        name.set("LINE Corporation")
                        email.set("dl_oss_dev@linecorp.com")
                        url.set("https://engineering.linecorp.com/en/")
                    }
                    developer {
                        id.set("pisces")
                        name.set("Steve Kim")
                        email.set("pisces@linecorp.com")
                    }
                }
                scm {
                    connection.set("scm:git@github.com:line/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:line/${rootProject.name}.git")
                    url.set("http://github.com/line/${rootProject.name}")
                }
            }
        }
    }
}