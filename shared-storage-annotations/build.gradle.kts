import org.jetbrains.kotlin.cli.common.toBooleanLenient

val kspVersion: String by project
val groupId: String by project

plugins {
    id("maven-publish")
    id("signing")
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

publishing {
    publications {
        create<MavenPublication>("kmmSharedStorageAnnotations") {
            artifactId = "kmm-shared-storage-annotations"
            from(components["java"])
            artifact(tasks["sourcesJar"])
            pom {
                description.set("Shared Storage Annotations for Kotlin Multiplatform Mobile")
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    println("signingKey, signingPassword -> ${signingKey?.slice(0..9)}, ${signingPassword?.map { "*" }?.joinToString("")}")

    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["kmmSharedStorageAnnotations"])
}