
@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.lombok

plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Lombok in main
    "compileOnly"(libs.findLibrary("lombok").get())
    "annotationProcessor"(libs.findLibrary("lombok").get())

    // Lombok in tests
    "testCompileOnly"(libs.findLibrary("lombok").get())
    "testAnnotationProcessor"(libs.findLibrary("lombok").get())
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.isDeprecation = true
    options.compilerArgs.add("-parameters")
}