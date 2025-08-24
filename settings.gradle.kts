

pluginManagement {
    repositories { gradlePluginPortal(); mavenCentral() }
}

plugins {
    // Apply Foojay settings plugin here (settings script only)
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


rootProject.name = "IfStatements"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { mavenCentral() }
}


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
includeBuild("build-logic")