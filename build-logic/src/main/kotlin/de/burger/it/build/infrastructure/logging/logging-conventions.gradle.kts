@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.logging

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.`java-library`
import org.gradle.kotlin.dsl.the

plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Facade (compile-time)
    "api"(libs.findLibrary("slf4j-api").get())

    // Bridges (route legacy frameworks into SLF4J)
    "implementation"(libs.findLibrary("jul-to-slf4j").get())
    "implementation"(libs.findLibrary("jcl-over-slf4j").get())

    // SLF4J -> Log4j2 binding (runtime only)
    "runtimeOnly"(libs.findLibrary("log4j-slf4j2-impl").get())

    // Core backend
    "runtimeOnly"(libs.findLibrary("log4j-core").get())
    "runtimeOnly"(libs.findLibrary("log4j-api").get())
}

configurations.all {
    // Global excludes to avoid multiple bindings/conflicts
    exclude(group = "ch.qos.logback", module = "logback-classic")
    exclude(group = "ch.qos.logback", module = "logback-core")
    exclude(group = "org.slf4j", module = "slf4j-simple")
    exclude(group = "org.slf4j", module = "slf4j-reload4j")
    // Prevent loops: do NOT allow log4j-to-slf4j when using log4j-slf4j2-impl
    exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
}

tasks.withType<Test>().configureEach {
    // Ensure bridges active during tests
    jvmArgs("-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager")
}