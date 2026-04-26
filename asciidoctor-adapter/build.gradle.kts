dependencies {
    api(project(":docbook-2-docx-common"))
    api("org.asciidoctor:asciidoctorj:${property("asciidoctorjVersion")}")
    api("org.asciidoctor:asciidoctorj-api:${property("asciidoctorjVersion")}")
}
