import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.devtools.ksp")
}

val localProps = Properties().apply {
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        load(FileInputStream(localPropsFile))
    }
}

kotlin {
    androidTarget {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.constraintlayout)

            // Ktor engine for Android
            implementation(libs.ktor.client.okhttp)

            // Compose dependencies (Android specific)
            implementation("androidx.activity:activity-compose:1.8.0")

            // DataStore (proto - Android only)
            implementation("androidx.datastore:datastore:1.2.0")

            implementation(libs.androidx.work.runtime.ktx)

            //Glance
            implementation("androidx.glance:glance-appwidget:1.1.1")
            implementation("androidx.glance:glance-material3:1.1.1")
            implementation("androidx.glance:glance-material:1.1.1")

            //splashScreen
            implementation("androidx.core:core-splashscreen:1.0.1")

            //EncryptedSharedPreferences
            implementation("androidx.security:security-crypto:1.1.0-alpha06")

            //PlayCore
            implementation("com.google.android.play:app-update:2.1.0")
            implementation("com.google.android.play:app-update-ktx:2.1.0")

            //koin
            implementation(libs.koin.android)
        }
        
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.mp.material3)
            implementation(libs.compose.mp.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.mp.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            
            // Ktor (KMP HTTP Client)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            
            // Ksoup (KMP HTML/XML Parser)
            implementation(libs.ksoup)

            // Kotlinx DateTime
            implementation(libs.kotlinx.datetime)

            // DataStore Preferences KMP
            implementation(libs.datastore.preferences.core)

            // kotlinx-collections-immutable
            implementation(libs.kotlinx.collections.immutable)

            // kotlinx-serialization
            implementation(libs.kotlinx.serialization.json)

            // Haze (Frosted Glass)
            implementation(libs.haze)
            implementation(libs.haze.materials)

            // Navigation Compose
            implementation(libs.navigation.compose)

            // Room KMP
            implementation(libs.room.runtime)
            implementation(libs.compottie)
            
            // Icons (Explicit version to resolve build issue)
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.0")


            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.koin.navigation.compose)
        }

        iosMain.dependencies {
            // Ktor engine for iOS
            implementation(libs.ktor.client.darwin)
        }
        
        commonTest.dependencies {
             implementation(kotlin("test"))
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.composeUiTooling)
}

// Force Kotlin metadata library version for Dagger/Hilt compatibility with Kotlin 2.3.0
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-metadata-jvm") {
            useVersion("2.3.0")
        }
    }
}

android {
    namespace = "com.kito"
    compileSdk = 35
    ndkVersion = "26.1.10909125"

    defaultConfig {
        applicationId = "com.kito"
        minSdk = 26
        targetSdk = 35
        versionCode = 19
        versionName = "3.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "PORTAL_BASE",
            "\"https://kiitportal.kiituniversity.net\"",

            )
        buildConfigField(
            "String",
            "WD_PATH",
            "\"/sap/bc/webdynpro/sap/ZWDA_HRIQ_ST_ATTENDANCE\""
        )
        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${localProps.getProperty("SUPABASE_URL")}\""
        )

        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            "\"${localProps.getProperty("SUPABASE_ANON_KEY")}\""
        )
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "KIITO (Debug)")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            resValue("string", "app_name", "KIITO")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("internal_testing") {
            initWith(getByName("release"))

            resValue("string", "app_name", "KIITO (Testing)")
            applicationIdSuffix = ".testing"
            versionNameSuffix = "-testing"

            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }
}