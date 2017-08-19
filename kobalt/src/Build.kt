import java.nio.file.Paths
import java.nio.file.Files

import java.io.IOException

import com.beust.kobalt.project
import com.beust.kobalt.api.Project

import com.beust.kobalt.api.annotation.Task
import com.beust.kobalt.TaskResult

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
    version = "0.0.1"

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

        licenses = listOf(License().apply {
            name = "Apache 2.0"
            url = "https://apache.org/licenses/LICENSE-2.0"
        })

        scm = Scm().apply {
            url = "https://github.com/FDeityLink/KotlinUtils"
            connection = "https://github.com/FDeityLink/KotlinUtils.git"
            developerConnection = "git@github.com:FDeityLink/KotlinUtils.git"
        }

        developers = listOf(Developer().apply {
            name = "Brian Christian"
        })
    }
}

@Suppress("unused")
@Task(name = "removeJavadocJar", description = "Remove empty Javadoc JAR from output",
      alwaysRunAfter = arrayOf("assemble"))
fun taskRemoveJavadocJar(project: Project): TaskResult {
    //Dokka not configured and is buggy anyway
    val javadocJar = Paths.get(project.buildDirectory, "libs",
                               "${project.name}-${project.version}-javadoc.jar").toAbsolutePath()
    return try {
        Files.delete(javadocJar)
        println("Deleted $javadocJar")
        TaskResult()
    }
    catch (except: Exception) {
        when (except) {
            is IOException, is SecurityException -> {
                System.err.println("Caught ${except::class.simpleName} after attempting to delete $javadocJar")
                except.printStackTrace()

                TaskResult(false, errorMessage = except.localizedMessage)
            }

            else -> throw except
        }
    }
}