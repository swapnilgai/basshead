package com.org.basshead

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.extra
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties

class BuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            setApiProperties(loadLocalProperty())
        }
    }
}

internal fun Project.loadLocalProperty(file: String = "keystore.properties"): ProjectProperties {
    val projectProperties = Properties()
    val localProperties = File(file)
    if (localProperties.isFile) {
        FileInputStream(localProperties).use { fis ->
            val reader = InputStreamReader(fis, StandardCharsets.UTF_8)
            projectProperties.load(reader)
        }
    } else {
        throw GradleException("Missing $file")
    }
    return ProjectProperties(
        prodKey = projectProperties.getProperty("PROD_API_KEY"),
        devKey = projectProperties.getProperty("DEV_API_KEY")
    )
}

private fun Project.setApiProperties(projectProperties: ProjectProperties) {
    rootProject.extra.set("ProjectProperties", projectProperties)
}

fun Project.projectProperties(): Provider<ProjectProperties> = provider {
    rootProject.extra.get("ProjectProperties") as ProjectProperties
}
