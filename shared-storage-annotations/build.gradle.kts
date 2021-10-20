import org.jetbrains.kotlin.cli.common.toBooleanLenient

val kspVersion: String by project
val groupId: String by project

plugins {
    id("maven-publish")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    artifacts {
        archives(sourcesJar)
        archives(jar)
    }
}

val isMavenLocal = System.getProperty("maven.local").toBooleanLenient() ?: false
if (!isMavenLocal) {
    publishing {
        publications {
            create<MavenPublication>("NaverRepo") {
                artifactId = "shared-storage-annotations"
                from(components["java"])
                artifact(tasks["sourcesJar"])
                pom {
                    name.set("$groupId:$artifactId")
                    description.set("Shared Storage Annotations for Kotlin")
                }
            }
        }
    }
}