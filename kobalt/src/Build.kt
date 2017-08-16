import com.beust.kobalt.project
import com.beust.kobalt.plugin.packaging.assemble

@Suppress("unused")
val project = project {
    name = "KotlinUtils"
    group = "io.fdeitylink"
    artifactId = name
    version = "0.1"

    dependencies {
        provided("org.jetbrains.kotlin:kotlin-stdlib-jre8:1.1.4")
    }

    assemble {
        jar {  }
    }
}