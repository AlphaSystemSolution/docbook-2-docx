plugins {
    id("net.researchgate.release") version "3.1.0"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    alias(libs.plugins.spotless)
}

allprojects {
    group = "io.github.sfali23"
}

apply(from = "${rootDir}/scripts/nexus-publish.gradle")

configure<net.researchgate.release.ReleaseExtension> {
    tagTemplate.set("v\${version}")
}

afterEvaluate {
    tasks.named("afterReleaseBuild") {
        dependsOn("publishToSonatype", "closeAndReleaseSonatypeStagingRepository")
    }
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target("src/**/*.java")
            googleJavaFormat("1.35.0")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
            // Custom rule to replace 3+ newlines with just 2
            replaceRegex("Remove extra newlines", "\\n\\n\\n+", "\n\n")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    apply(from = "${rootDir}/scripts/publishing.gradle")

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        withJavadocJar()
        withSourcesJar()
    }
}
