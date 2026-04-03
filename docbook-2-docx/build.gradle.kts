dependencies {
    api(project(":docbook-model"))
    api(project(":docbook-2-docx-common"))
    api("org.glassfish.jaxb:jaxb-runtime:${property("jaxbRuntimeVersion")}")
    api("jakarta.xml.bind:jakarta.xml.bind-api:${property("bindApiVersion")}")
    api("io.github.sfali23:open-xml-builder:${property("openxmlBuilderVersion")}")
    api("io.vavr:vavr:${property("vavrVersion")}")
    api("org.graalvm.js:js:${property("graalvmJsVersion")}")
    api("com.typesafe:config:${property("typesafeConfigVersion")}")
    api("org.slf4j:slf4j-api:${property("slf4jApiVersion")}")
    api("ch.qos.logback:logback-classic:${property("logbackClassicVersion")}")
    testImplementation("org.testng:testng:${property("testngVersion")}")
    testImplementation("com.google.inject:guice:${property("guiceVersion")}")
    testImplementation("com.google.guava:guava:${property("guavaVersion")}")
    testImplementation("org.uncommons:reportng:${property("reportngVersion")}")
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
