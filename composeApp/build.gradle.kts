import com.org.basshead.ProjectProperties
import com.org.basshead.projectProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildKonfig)
    id("app-config-plugin")
    alias(libs.plugins.kotlinSerialization)
}

val projectProperties: ProjectProperties = projectProperties().get()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.atomicfu)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)
            implementation(libs.ktor.client.loggin)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
            api(libs.koin.core)
        }
    }
}

android {
    namespace = "com.org.basshead"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.org.basshead"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += "releaseType"
    productFlavors {
        create("internal") {
            dimension = "releaseType"
            applicationIdSuffix = ".internal"
            versionNameSuffix = "-releaseType"
            applicationId = "com.internal.basshead"
            signingConfig = null
        }
        create("production") {
            dimension = "releaseType"
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
            applicationId = "com.org.basshead"
            // signingConfig = signingConfig.getBaseName("ss")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            matchingFallbacks += "debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

buildkonfig {
    packageName = "com.org.basshead"
    defaultConfigs {}

    defaultConfigs("internalRelease") {
        val apiKey = projectProperties.devKey
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiKey", apiKey)
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "environment", "dev")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiUrl", projectProperties.devApiUrl)
    }
    defaultConfigs("internalDebug") {
        val apiKey = projectProperties.devKey
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiKey", apiKey)
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "environment", "dev")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiUrl", projectProperties.devApiUrl)
    }

    defaultConfigs("production") {
        val apiKey = projectProperties.prodKey
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiKey", apiKey)
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "environment", "prod")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "apiUrl", projectProperties.devApiUrl)
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
