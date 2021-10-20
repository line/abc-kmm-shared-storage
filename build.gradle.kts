import org.jetbrains.kotlin.cli.common.toBooleanLenient

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

val groupId: String by project
val libVersion: String by project
val isMavenLocal = System.getProperty("maven.local").toBooleanLenient() ?: false

subprojects {
    group = groupId
    version = libVersion

    repositories {
        mavenCentral()
        google()
    }

    if (!isMavenLocal) {
        pluginManager.withPlugin("maven-publish") {
            val isSnapshotUploadString: String by project
            val isSnapshotUpload = isSnapshotUploadString.toBoolean()
            val publishExtension = extensions.getByType<PublishingExtension>()
            publishExtension.repositories {
                maven {
                    isAllowInsecureProtocol = true
                    url = if (isSnapshotUpload) {
                        uri("http://repo.navercorp.com/m2-snapshot-repository")
                    } else {
                        uri("http://repo.navercorp.com/maven2")
                    }
                    credentials {
                        username = System.getProperty("maven.username") ?: ""
                        password = System.getProperty("maven.password") ?: ""
                    }
                }
            }

            publishExtension.publications.whenObjectAdded {
                check(this is MavenPublication) {
                    "unexpected publication $this"
                }

                groupId = groupId
                version = if (isSnapshotUpload) "$libVersion-SNAPSHOT" else libVersion

                pom {
                    name.set("$groupId:$artifactId")
                    url.set("https://github.com/line/${rootProject.name}")

                    developers {
                        developer {
                            id.set("pisces")
                            name.set("Steve Kim")
                            email.set("pisces@linecorp.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:ssh://github.com/line/${rootProject.name}.git")
                        developerConnection.set("scm:git:ssh://github.com/line/${rootProject.name}.git")
                        url.set("http://github.com/line/${rootProject.name}")
                    }
                }
            }
        }
    }
}

println("##teamcity[setParameter name='postbuild.version' value='${libVersion}']")