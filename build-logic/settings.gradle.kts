
pluginManagement {
    repositories { gradlePluginPortal(); mavenCentral() }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { mavenCentral() }
    versionCatalogs {
        // Einmalig: bindet den zentralen Catalog aus dem Root ein
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
