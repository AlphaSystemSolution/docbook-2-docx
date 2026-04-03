plugins {
    id("net.researchgate.release") version "3.1.0"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    group = "io.github.sfali23"
}

apply(from = "${rootDir}/scripts/nexus-publish.gradle.kts")

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "java-library")
    apply(plugin = "jacoco")

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
        }
    }

    apply(from = "${rootDir}/scripts/publishing.gradle.kts")

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        withJavadocJar()
        withSourcesJar()
    }

    configure<net.researchgate.release.ReleaseExtension> {
        tagTemplate.set("v\${version}")
    }

    afterEvaluate {
        tasks.named("afterReleaseBuild") {
            dependsOn("publishToSonatype", "closeAndReleaseSonatypeStagingRepository")
        }
    }
}
