plugins {
    alias(libs.plugins.xjc)
}

dependencies {
    api(libs.jaxbCore)
    api(libs.jaxbRuntime)
    api(libs.jaxbImpl)
    api(libs.xmlBindApi)
    xjc(libs.cxfXjcDv)
    xjc(libs.jaxbXewPlugin)
    xjc(libs.jaxb2FluentApi)
    xjc(libs.jaxb2DefaultValue)
    xjc(libs.jaxb2BasicsAnnotate)
    xjc(libs.collectionSetterInjector)
}

configure<com.github.bjornvester.xjc.XjcExtension> {
    bindingFiles = project.files("$projectDir/src/main/resources/docbook.xjb")
    options.add("-Xfluent-api")
    options.add("-Xdefault-value")
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.get()}/generated/sources/xjc/java")
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
