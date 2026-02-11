import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.devtools.ksp")
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

            //EncryptedSharedPreferences
            implementation("androidx.security:security-crypto:1.1.0-alpha06")

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

            // Navigation 3
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)
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
    namespace = "com.kito.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug { }
        release { }
        create("internal_testing") {
            initWith(getByName("release"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}