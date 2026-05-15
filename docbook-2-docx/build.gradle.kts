dependencies {
    api(project(":docbook-model"))
    api(project(":docbook-2-docx-common"))
    api("io.github.sfali23:docx4j-builder:${libs.versions.docx4jBuilder.get()}")
    api("io.vavr:vavr:${libs.versions.vavr.get()}")
    api("org.graalvm.js:js:${libs.versions.graalvmJs.get()}")
    api("org.graalvm.polyglot:polyglot:${libs.versions.graalvmJs.get()}")
    api("com.typesafe:config:${libs.versions.typesafeConfig.get()}")
    api("org.slf4j:slf4j-api:${libs.versions.slf4jApi.get()}")
    api("ch.qos.logback:logback-classic:${libs.versions.logbackClassic.get()}")
    testImplementation("org.testng:testng:${libs.versions.testng.get()}")
    testImplementation("com.google.inject:guice:${libs.versions.guice.get()}")
    testImplementation("com.google.guava:guava:${libs.versions.guava.get()}")
    testImplementation("org.uncommons:reportng:${libs.versions.reportng.get()}")
}

tasks.withType<Test>().configureEach {
    systemProperty("docbook-docx.styles", "META-INF/custom-styles.xml")
    systemProperty("target.path", "build/docs")
    systemProperty("data.path", "${projectDir}/src/test/resources/data")
    systemProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog")
}

tasks.named<Test>("test") {
    useTestNG {
        suites("testng/testng.xml")
    }
    options {
        this as TestNGOptions
        listeners.add("org.uncommons.reportng.HTMLReporter")
        listeners.add("org.uncommons.reportng.JUnitXMLReporter")
    }
}

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required.set(true)
        html.required.set(false)
    }
}

tasks.named("check") {
    dependsOn("jacocoTestReport")
}
