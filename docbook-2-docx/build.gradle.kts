plugins {
    `java-test-fixtures`
}

// keep test fixtures local: don't publish the testFixtures variants/artifacts
(components["java"] as AdhocComponentWithVariants).apply {
    withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
    withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
}

dependencies {
    api(project(":docbook-model"))
    api(project(":docbook-2-docx-common"))
    testFixturesImplementation(libs.testng)
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
    systemProperty("config.file", "${rootDir}/custom-scripts/docbook-docx.conf")
    systemProperty("docbook-docx.customDirPath", "${rootDir}/custom-scripts")
    systemProperty("target.path", "build/docs")
    systemProperty("data.path", "${projectDir}/src/test/resources/data")
    systemProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog")
}

// java-test-fixtures puts this project's jars on the test classpath; swap them for
// the output dirs so ApplicationController can walk META-INF/scripts as a directory
// (AppUtil.processResourceDirectory fails on jar URLs)
val ownJars = fileTree(layout.buildDirectory.dir("libs"))
tasks.named<Test>("test") {
    classpath =
        classpath - ownJars + sourceSets.main.get().output + sourceSets.testFixtures.get().output
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
