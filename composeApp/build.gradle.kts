import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.dagger.hilt.android")
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

            // Dependencies for SAP portal integration
            implementation("com.squareup.okhttp3:okhttp:4.12.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
            implementation("org.jsoup:jsoup:1.17.2")

            // Compose dependencies for modern UI (Android specific if not yet moved to common)
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.composeUi)
            implementation(libs.composeUiTooling)
            implementation(libs.composeUiGraphics)
            implementation(libs.compose.material3)
            implementation(libs.androidx.activity)
            implementation("androidx.activity:activity-compose:1.8.0")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

            // if using Compose BOM, keep versions aligned
            implementation("androidx.compose.material:material-icons-extended")

            // DataStore for persistent preferences
            implementation("androidx.datastore:datastore-preferences:1.2.0")
            implementation("androidx.datastore:datastore:1.2.0")
            implementation(libs.androidx.navigation.compose)
            implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")

            //Dagger - Hilt
            implementation(libs.hilt.android)
            implementation(libs.androidx.work.runtime.ktx)
            
            implementation(libs.androidx.hilt.navigation.compose)

            //Lottie Animation
            implementation("com.airbnb.android:lottie-compose:6.6.7")

            //HazeEffect(Frosted Glass)
            implementation(libs.haze)
            implementation(libs.haze.materials)

            //Glance
            implementation("androidx.glance:glance-appwidget:1.1.1")
            // For interop APIs with Material 3
            implementation("androidx.glance:glance-material3:1.1.1")
            // For interop APIs with Material 2
            implementation("androidx.glance:glance-material:1.1.1")

            //composeNavigation
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)

            //splashScreen
            implementation("androidx.core:core-splashscreen:1.0.1")

            // Room (Database)
            implementation("androidx.room:room-runtime:2.7.0")
            implementation("androidx.room:room-ktx:2.7.0")
            implementation("androidx.room:room-paging:2.7.0")

            //retrofit
            implementation("com.squareup.retrofit2:retrofit:2.11.0")
            implementation("com.squareup.retrofit2:converter-gson:2.11.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

            //EncryptedSharedPreferences
            implementation("androidx.security:security-crypto:1.1.0-alpha06")

            //PlayCore
            implementation("com.google.android.play:app-update:2.1.0")
            implementation("com.google.android.play:app-update-ktx:2.1.0")

            //System UI Controller
            implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
            
            // KMP Compose Android dependencies
             implementation(libs.compose.mp.uiToolingPreview) // Android specific preview
             implementation("androidx.activity:activity-compose:1.8.0")
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
        }
        
        commonTest.dependencies {
             implementation(kotlin("test"))
        }
    }
}

dependencies {
    add("kspAndroid", libs.hilt.android.compiler)
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")

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
        minSdk = 24
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