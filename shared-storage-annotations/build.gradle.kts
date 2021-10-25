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

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("kmmSharedStorageAnnotations") {
            artifactId = "kmm-shared-storage-annotations"
            from(components["java"])
            pom {
                description.set("Shared Storage Annotations for Kotlin Multiplatform Mobile")
            }
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
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