// gradle/ktlint.gradle.kts

repositories {
    mavenCentral()
}

val ktlint by configurations.creating {
    attributes {
        attribute(
            org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE,
            objects.named(org.gradle.api.attributes.Bundling::class.java, "shadowed"),
        )
    }
}

dependencies {
    ktlint("com.pinterest:ktlint:0.50.0") {
        // Optional: Exclude conflicting dependencies
        exclude(group = "com.google.guava", module = "guava")
    }
}

val kotlinFiles = fileTree(
    mapOf(
        "dir" to projectDir,
        "include" to listOf("**/*.kt", "**/*.kts"),
        "exclude" to listOf("**/build/**", "**/generated/**"),
    ),
)

tasks.register<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Check Kotlin code style with ktlint 0.50.0"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(kotlinFiles.files.map { it.relativeTo(projectDir).path })
    inputs.files(kotlinFiles)
    jvmArgs = listOf(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
    )
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Auto-format Kotlin code with ktlint 0.50.0"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(listOf("-F") + kotlinFiles.files.map { it.relativeTo(projectDir).path })
    inputs.files(kotlinFiles)
    jvmArgs = listOf(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
    )
}
