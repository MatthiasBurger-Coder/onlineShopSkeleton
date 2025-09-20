@file:Suppress("UnstableApiUsage")

package de.burger.it.build.domain


plugins {
    // Keep it minimal: no java-library, no test config here.
    `java-base`
}

val libs = the<VersionCatalogsExtension>().named("libs")
val javaVersion = libs.findVersion("java").get().requiredVersion.toInt()
val forensicsDebug = providers.gradleProperty("forensicsDebug")
    .map { it.toBoolean() }
    .getOrElse(false)

extensions.configure<JavaPluginExtension> {
    // Use the JDK toolchain version from the central version catalog (gradle/libs.versions.toml).
    toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion)) }
}

tasks.withType<JavaCompile>().configureEach {
    // Keep source encoding consistent across modules.
    options.encoding = "UTF-8"

    // Generate bytecode for the configured toolchain version.
    // If you prefer a lower target (e.g., 21), add a separate "bytecode" version to the catalog and use that here.
    options.release.set(javaVersion)
    if (forensicsDebug) {
        // Enable full debug info for Byteman AT LINE / locals
        options.isDebug = true
        options.debugOptions.debugLevel = "source,lines,vars"
    }
}
