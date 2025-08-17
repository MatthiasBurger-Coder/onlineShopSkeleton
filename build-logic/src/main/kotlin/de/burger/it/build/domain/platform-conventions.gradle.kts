@file:Suppress("UnstableApiUsage")

package de.burger.it.build.domain

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType

plugins {
    // Keep it minimal: no java-library, no test config here.
    `java-base`
}

val libs = the<VersionCatalogsExtension>().named("libs")
val javaVersion = libs.findVersion("java").get().requiredVersion.toInt()

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
}
