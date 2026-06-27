dependencies {
    api(project(":docbook-model"))
    api(project(":docbook-2-docx-common"))
    api(libs.docx4jBuilder)
    api(libs.vavr)
    api(libs.graalvmJs)
    api(libs.polyglot)
    api(libs.typesafeConfig)
    api(libs.slf4jApi)
    api(libs.logbackClassic)
    testImplementation(libs.testng)
    testImplementation(libs.guice)
    testImplementation(libs.guava)
    testImplementation(libs.reportng)
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
