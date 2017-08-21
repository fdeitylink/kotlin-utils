import com.beust.kobalt.project

import com.beust.kobalt.plugin.packaging.assemble

import org.apache.maven.model.Model
import org.apache.maven.model.License
import org.apache.maven.model.Scm
import org.apache.maven.model.Developer

val kotlinVersion = "1.1.4-2"

@Suppress("unused")
val project = project {
    name = "kotlin-utils"
    group = "com.github.fdeitylink"
    artifactId = name
    version = "0.0.2"

    dependencies {
        provided("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    }

    assemble {
        mavenJars { }
    }

    pom = Model().apply {
        groupId = project.group
        name = project.name
        version = project.version

        description = "Utility functions and classes for Kotlin and Java programs"
        url = "https://github.com/FDeityLink/KotlinUtils"

        licenses = listOf(license {
            name = "Apache 2.0"
            url = "https://apache.org/licenses/LICENSE-2.0"
        })

        scm = scm {
            url = "https://github.com/FDeityLink/KotlinUtils"
            connection = "https://github.com/FDeityLink/KotlinUtils.git"
            developerConnection = "git@github.com:FDeityLink/KotlinUtils.git"
        }

        developers = listOf(developer {
            name = "Brian Christian"
        })
    }
}

fun license(init: License.() -> Unit) = License().apply { init() }

fun scm(init: Scm.() -> Unit) = Scm().apply { init() }

fun developer(init: Developer.() -> Unit) = Developer().apply { init() }