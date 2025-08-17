@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.spring


plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Only spring-test on test classpath
    "testImplementation"(libs.findLibrary("spring-test").get())
}
