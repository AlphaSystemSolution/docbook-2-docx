configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.sfali23"
            from(components["java"])
            pom {
                name.set("AlphaSystemCommons")
                description.set("Alpha system commons library")
                url.set("https://github.com/AlphaSystemSolution/open-xml-builder")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("sfali23")
                        name.set("Farhan Syed Ali")
                        email.set("f.syed.ali@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/AlphaSystemSolution/open-xml-builder.git")
                    developerConnection.set("scm:git:ssh//github.com:AlphaSystemSolution/open-xml-builder.git")
                    url.set("https://github.com/AlphaSystemSolution/open-xml-builder")
                }
            }
        }
    }
}

configure<SigningExtension> {
    useInMemoryPgpKeys(
        extra["signingKeyId"] as String,
        extra["signingKey"] as String,
        extra["signingPassword"] as String
    )
    sign(the<PublishingExtension>().publications)
}
