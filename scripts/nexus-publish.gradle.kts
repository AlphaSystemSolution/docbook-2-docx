import java.nio.file.Paths
import java.util.Properties

extra["signingKeyId"] = ""
extra["signingPassword"] = ""
extra["signingKey"] = ""
extra["ossrhUsername"] = ""
extra["ossrhPassword"] = ""
extra["sonatypeStagingProfileId"] = ""

val secretPropsFile = Paths.get(System.getProperty("user.home"), ".gradle", "sonatype.properties").toFile()

val p = Properties()
secretPropsFile.inputStream().use { p.load(it) }
p.forEach { name, value -> extra[name as String] = value }

configure<io.github.gradlenexus.publishplugin.NexusPublishExtension> {
    repositories {
        sonatype {
            stagingProfileId.set(extra["sonatypeStagingProfileId"] as String)
            username.set(extra["ossrhUsername"] as String)
            password.set(extra["ossrhPassword"] as String)
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
