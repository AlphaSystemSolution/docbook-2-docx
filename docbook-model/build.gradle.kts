plugins {
    id("com.github.bjornvester.xjc") version "1.9.0"
}

dependencies {
    api("org.glassfish.jaxb:jaxb-core:${libs.versions.jaxbRuntime.get()}")
    api("com.sun.xml.bind:jaxb-impl:${libs.versions.jaxbImpl.get()}")
    api("org.glassfish.jaxb:jaxb-runtime:${libs.versions.jaxbRuntime.get()}")
    api("jakarta.xml.bind:jakarta.xml.bind-api:${libs.versions.bindApi.get()}")
    xjc("org.apache.cxf.xjcplugins:cxf-xjc-dv:${libs.versions.cfxXjcPlugin.get()}")
    xjc("com.github.jaxb-xew-plugin:jaxb-xew-plugin:${libs.versions.jaxbXewPlugin.get()}")
    xjc("org.jvnet.jaxb2_commons:jaxb2-fluent-api:${libs.versions.jaxb2FluentApi.get()}")
    xjc("org.jvnet.jaxb2_commons:jaxb2-default-value:${libs.versions.jaxb2DefaultValue.get()}")
    xjc("org.jvnet.jaxb2_commons:jaxb2-basics-annotate:${libs.versions.jaxb2BasicsAnnotate.get()}")
    xjc("net.java.dev.vcc.thirdparty:collection-setter-injector:${libs.versions.collectionSetterInjector.get()}")
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
