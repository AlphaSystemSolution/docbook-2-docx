import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
    java
}

// Keep the shadowJar task available for local/CI builds, but do not add the
// shadow JAR variant to the Maven publication. The module is published as a
// standard Java library (thin JAR + POM dependencies).
shadow {
    addShadowVariantIntoJavaComponent = false
}

dependencies {
    api(project(":asciidoctor-adapter"))
    api(project(":docbook-2-docx"))
    api(libs.commonsCli)
}

// Task to merge reference.conf files manually
tasks.register("mergeReferenceConf") {
    dependsOn(":docbook-2-docx:jar")

    doLast {
        val mergedFile = file("${layout.buildDirectory.get()}/resources/main/reference.conf")
        mergedFile.parentFile.mkdirs()
        
        val confFiles = mutableListOf<String>()
        
        // Read directly from source files
        listOf(
            file("${rootDir}/docbook-2-docx/src/main/resources/reference.conf"),
            file("${rootDir}/arabic-handler/src/main/resources/reference.conf")
        ).forEach { srcFile ->
            if (srcFile.exists()) {
                confFiles.add(srcFile.readText())
                println("Found reference.conf in ${srcFile.name}")
            }
        }
        
        // Merge all configs
        if (confFiles.isNotEmpty()) {
            mergedFile.writeText(confFiles.joinToString("\n"))
            println("Merged ${confFiles.size} reference.conf files into ${mergedFile.path}")
            println("Total lines: ${mergedFile.readLines().size}")
        } else {
            println("WARNING: No reference.conf files found to merge!")
        }
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes("Main-Class" to "com.alphasystem.docx.cli.Main")
    }
}

tasks.named<ShadowJar>("shadowJar") {
    dependsOn("mergeReferenceConf")
    mergeServiceFiles()
    
    // Include the merged reference.conf
    from("${layout.buildDirectory.get()}/resources/main") {
        include("reference.conf")
    }
    
    manifest {
        attributes("Main-Class" to "com.alphasystem.docx.cli.Main")
    }
    
    // Ensure we're not excluding the reference.conf
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}
