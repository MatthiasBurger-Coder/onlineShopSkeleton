pluginManagement {
    repositories { gradlePluginPortal(); mavenCentral() }
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
