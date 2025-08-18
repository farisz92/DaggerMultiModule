plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.daggermultimodule"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.daggermultimodule"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:core"))

    implementation(project(":activity-one"))
    implementation(project(":activity-two"))
    implementation(project(":ui"))

    implementation(project(":features:steps:api"))
    implementation(project(":features:steps:dagger"))
    implementation(project(":features:steps:core"))

    heartRateDependencies(project)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)

    // Java annotation
    implementation(libs.java.x.annotation)

    // AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Material3
    implementation(libs.androidx.material3)  // Correct reference for Material3 library

    // UI and graphics
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

val useLocalHeartRate = project.findProperty("useLocalHeartRate")?.toString()?.toBoolean() ?: false