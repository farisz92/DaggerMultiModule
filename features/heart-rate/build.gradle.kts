import com.android.build.gradle.internal.api.LibraryVariantOutputImpl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.features.heartrate"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

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

    libraryVariants.all {
        outputs.all {
            (this as? LibraryVariantOutputImpl)?.outputFileName =
                "heartrate-${buildType.name}.aar"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(project(":core:di"))
    api(project(":features:heart-rate:api"))
    api(project(":features:heart-rate:dagger"))
    implementation(project(":features:heart-rate:core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register("createFatAarWithCore") {
    dependsOn(":core:di:assembleDebug")
    dependsOn("createFatAar")

    doLast {
        val buildDir = layout.buildDirectory.get().asFile
        val fatAar = file("${buildDir}/outputs/aar/heartrate-fat-debug.aar")
        val coreAar = file("${project(":core:di").buildDir}/outputs/aar/core-di-debug.aar")

        if (coreAar.exists()) {
            println("Core AAR found: ${coreAar.absolutePath}")
            println("You can manually merge core classes if needed")
            // Could add manual merge logic here
        }
    }
}


val combineClassesTask = tasks.register<Zip>("combineClasses") {
    dependsOn("assembleDebug")
    dependsOn(":core:di:assembleDebug")  // Add this
    dependsOn(":features:heart-rate:api:assembleDebug")
    dependsOn(":features:heart-rate:core:assembleDebug")
    dependsOn(":features:heart-rate:dagger:assembleDebug")

    archiveFileName.set("classes.jar")
    destinationDirectory.set(file("${buildDir}/tmp"))

    val submoduleAars = listOf(
        file("${project(":core:di").buildDir}/outputs/aar/di-debug.aar"),  // Add this
        file("${project(":features:heart-rate:api").buildDir}/outputs/aar/api-debug.aar"),
        file("${project(":features:heart-rate:core").buildDir}/outputs/aar/core-debug.aar"),
        file("${project(":features:heart-rate:dagger").buildDir}/outputs/aar/dagger-debug.aar")
    )

    submoduleAars.forEach { aarFile ->
        from({
            if (aarFile.exists()) {
                println("Including classes from: ${aarFile.name}")
                val aarTree = zipTree(aarFile)
                val classesFiles = aarTree.matching { include("classes.jar") }
                if (!classesFiles.isEmpty) {
                    zipTree(classesFiles.singleFile)
                } else {
                    println("No classes.jar in ${aarFile.name}")
                    files() // empty
                }
            } else {
                println("AAR not found: ${aarFile.absolutePath}")
                files() // empty
            }
        })
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

// Then create the fat AAR
tasks.register<Zip>("createFatAar") {
    dependsOn(combineClassesTask)

    archiveFileName.set("heartrate-fat-debug.aar")
    destinationDirectory.set(file("${buildDir}/outputs/aar"))

    val originalAar = file("${buildDir}/outputs/aar/heartrate-debug.aar")

    // Include everything from original AAR except classes.jar
    from(zipTree(originalAar)) {
        exclude("classes.jar")
    }

    // Include the combined classes.jar
    from(combineClassesTask.get().archiveFile) {
        rename { "classes.jar" }
    }

    doLast {
        println("Fat AAR created: ${archiveFile.get().asFile.absolutePath}")
    }
}
