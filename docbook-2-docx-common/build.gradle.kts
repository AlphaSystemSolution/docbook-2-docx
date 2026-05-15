dependencies {
    api("io.github.sfali23:commons:${libs.versions.commons.get()}")

    // to fix transitive dependency venerability
    api("org.apache.commons:commons-lang3:${libs.versions.commonsLang3.get()}")
}
