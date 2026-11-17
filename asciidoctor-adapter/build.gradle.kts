dependencies {
    api(project(":docbook-2-docx-common"))
    api(libs.asciidoctorj)
    api(libs.asciidoctorjApi)
    // Use build output dirs instead of the jar so ApplicationController can walk
    // META-INF/scripts as a directory (AppUtil.processResourceDirectory fails on jar URLs)
    testImplementation(files("${rootDir}/docbook-2-docx/build/classes/java/main"))
    testImplementation(files("${rootDir}/docbook-2-docx/build/resources/main"))
    testImplementation(files("${rootDir}/docbook-2-docx/build/classes/java/testFixtures"))
    testImplementation(project(":docbook-model"))
    testImplementation(libs.docx4jBuilder)
    testImplementation(libs.vavr)
    testImplementation(libs.graalvmJs)
    testImplementation(libs.polyglot)
    testImplementation(libs.typesafeConfig)
    testImplementation(libs.slf4jApi)
    testImplementation(libs.logbackClassic)
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
    systemProperty("output.file.name", "asciidoc_to_docx.docx")
    systemProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog")
}

tasks.named("compileTestJava") {
    dependsOn(":docbook-2-docx:classes", ":docbook-2-docx:testFixturesClasses")
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
