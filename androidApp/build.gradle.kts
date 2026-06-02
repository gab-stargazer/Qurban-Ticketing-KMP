import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "org.lelestacia.qurban_ticketing"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.lelestacia.qurban_ticketing"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xskip-prerelease-check")
    }
    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    dependencies {
        implementation(projects.composeApp)
        implementation(libs.androidx.activity.compose)
        implementation(libs.compose.uiToolingPreview)

        implementation(libs.compose.uiToolingPreview)
        implementation(libs.androidx.activity.compose)

        //  Accompanist
        implementation("com.google.accompanist:accompanist-permissions:0.37.3")

        //  Koin
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)
        implementation(libs.koin.workmanager)

        //  Workmanager
        implementation(libs.workmanager)

        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
        implementation(libs.compose.material3)
        implementation(libs.compose.ui)
        implementation(libs.compose.components.resources)
        implementation(libs.compose.uiToolingPreview)
        implementation(libs.androidx.lifecycle.viewmodelCompose)
        implementation(libs.androidx.lifecycle.runtimeCompose)
        implementation(libs.material.icons.extended)

        //  Arrow
        implementation(project.dependencies.platform(libs.arrow.bom))
        implementation(libs.arrow.core)
        implementation(libs.arrow.fx.coroutines)
        implementation(libs.arrow.optics)

        //  Filekit
        implementation(libs.filekit.dialogs)
        implementation(libs.filekit.dialogs.compose)

        //  Koin
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.navigation3)

        //  Lifecycle
        implementation(libs.jetbrains.material3.adaptiveNavigation3)
        implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

        //  Navigation
        implementation(libs.jetbrains.navigation3.ui)

        //  Paging
        implementation(libs.paging)
        implementation(libs.paging.compose)

        //  Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.sqlite.bundled)
        implementation(libs.androidx.room.paging)

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
        implementation(libs.kotlinx.datetime)

        //  Kermit
        implementation(libs.kermit)

        //  KmpFile
        implementation("dev.zwander:kmpfile:0.8.0")

        //   Itext
        implementation(libs.itext)
    }
}