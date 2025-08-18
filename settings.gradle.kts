pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("app/libs")
        }
    }
}

rootProject.name = "DaggerMultiModule"
include(":app")
include(":features:steps")
include(":features:steps:api")
include(":features:steps:dagger")
include(":features:steps:core")
include(":features:heart-rate")
include(":features:heart-rate:api")
include(":features:heart-rate:dagger")
include(":features:heart-rate:core")
include(":ui")
include(":activity-one")
include(":activity-two")
include(":core:di")
include(":core:core")
