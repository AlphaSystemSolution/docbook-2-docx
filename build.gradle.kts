plugins {
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.semverRelease)
    alias(libs.plugins.spotless)
}

allprojects {
    group = "io.github.sfali23"
}

val isLocalPublish = gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal") }

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "com.vanniktech.maven.publish")

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

    configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        publishToMavenCentral(automaticRelease = true)
        if (!isLocalPublish) {
            signAllPublications()
        }

        coordinates("io.github.sfali23", project.name)

        pom {
            name.set("DocBook to Docx")
            description.set("DocBook to Docx converter")
            url.set("https://github.com/AlphaSystemSolution/docbook-2-docx")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("sfali23")
                    name.set("Syed Farhan Ali")
                    email.set("f.syed.ali@gmail.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/AlphaSystemSolution/docbook-2-docx.git")
                developerConnection.set("scm:git:ssh://github.com/AlphaSystemSolution/docbook-2-docx.git")
                url.set("https://github.com/AlphaSystemSolution/docbook-2-docx")
            }
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
        withSourcesJar()
    }
}

semverrelease {
    addUnReleasedCommitsToTagComment.set(true)
}

tasks.named("setReleaseVersion") {
    doLast {
        subprojects { version = rootProject.version }
    }
}
