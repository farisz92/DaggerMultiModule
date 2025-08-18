import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.heartRateDependencies(project: Project) {
    val useLocal = project.findProperty("useLocalHeartRate")?.toString()?.toBoolean() ?: true

    if (useLocal) {
        // Local project dependencies - must be direct references
        add("implementation", project.project(":features:heart-rate:api"))
        add("implementation", project.project(":features:heart-rate:dagger"))
        add("implementation", project.project(":features:heart-rate:core"))
    } else {
        // AAR dependency - also direct reference
        add("implementation", ":heartrate-fat-debug@aar")
    }
}
