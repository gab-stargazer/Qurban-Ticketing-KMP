import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
//    alias(libs.plugins.kotzilla)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexplicit-backing-fields",
            "-Xskip-prerelease-check"
        )
    }

    android {
        namespace = "org.lelestacia.qurban_ticketing.library"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.material.icons.extended)

            //  Apache POI
            implementation("org.apache.poi:poi:5.5.1")
            implementation("org.apache.poi:poi-ooxml:5.5.1")

            //  Arrow
            api(project.dependencies.platform(libs.arrow.bom))
            api(libs.arrow.core)
            api(libs.arrow.fx.coroutines)
            api(libs.arrow.optics)

            //  Filekit
            api(libs.filekit.dialogs)
            api(libs.filekit.dialogs.compose)

            //  Koin
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            api(libs.koin.navigation3)

            //  Lifecycle
            api(libs.jetbrains.material3.adaptiveNavigation3)
            api(libs.jetbrains.lifecycle.viewmodelNavigation3)

            //  Navigation
            api(libs.jetbrains.navigation3.ui)

            //  Paging
            api(libs.paging)
            api(libs.paging.compose)

            //  Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.room.paging)

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
            implementation(libs.kotlinx.datetime)

            //  Kermit
            api(libs.kermit)

            //  Retable
            implementation(libs.retable)

            //  KmpFile
            implementation("dev.zwander:kmpfile:0.8.0")

            //   Itext
            implementation(libs.itext)

            //  Kotzilla
//            implementation(libs.kotzilla.sdk.compose)


            //  Datastore
            api("androidx.datastore:datastore:1.2.1")
            api("androidx.datastore:datastore-preferences:1.2.1")

            //  Coil
            api("io.coil-kt.coil3:coil-compose:3.4.0")
            api("io.coil-kt.coil3:coil-network-okhttp:3.4.0")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

        }
    }
}

compose.resources {
    publicResClass = true
}

//android {
//    namespace = "org.lelestacia.qurban_ticketing"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//
//    defaultConfig {
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }
//
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            excludes += "META-INF/io.netty.versions.properties"
//            excludes += "META-INF/INDEX.LIST"
//            excludes += "META-INF/DEPENDENCIES"
//        }
//    }
//
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    kspCommonMainMetadata(libs.androidx.room.compiler)
    kspCommonMainMetadata(libs.arrow.optics.compiler)

//    add("kspCommonMainMetadata", libs.arrow.optics.compiler)
//    add("kspJvm", libs.androidx.room.compiler)
//    add("kspAndroid", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

//kotzilla {
//    versionName = "1.0.0" // Your app version
//}

compose.desktop {
    application {
        mainClass = "org.lelestacia.qurban_ticketing.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.lelestacia.qurban_ticketing"
            packageVersion = "1.0.0"
        }
    }
}
