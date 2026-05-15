dependencies {
    api(project(":docbook-2-docx-common"))
    api("org.asciidoctor:asciidoctorj:${libs.versions.asciidoctorj.get()}")
    api("org.asciidoctor:asciidoctorj-api:${libs.versions.asciidoctorj.get()}")
}
