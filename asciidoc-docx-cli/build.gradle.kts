import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.4.1"
    java
}

dependencies {
    api(project(":asciidoctor-adapter"))
    api(project(":docbook-2-docx"))
    api(project(":arabic-handler"))
    api("commons-cli:commons-cli:${property("commonsCliVersion")}")
}

// Task to merge reference.conf files manually
tasks.register("mergeReferenceConf") {
    dependsOn(":docbook-2-docx:jar")
    dependsOn(":arabic-handler:jar")
    
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
    dependsOn(":docbook-2-docx-common:jar")
    dependsOn(":docbook-model:jar")
    dependsOn(":asciidoctor-adapter:jar")
    dependsOn(":docbook-2-docx:jar")
    dependsOn(":arabic-handler:jar")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes("Main-Class" to "com.alphasystem.docx.cli.Main")
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
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
