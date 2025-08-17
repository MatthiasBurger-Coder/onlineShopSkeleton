@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.jetbrains

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.`java-library`

plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Libraries: expose annotations to consumers (compileOnlyApi)
plugins.withType(JavaLibraryPlugin::class) {
    dependencies {
        "compileOnlyApi"(libs.findLibrary("annotations").get())
        "testCompileOnly"(libs.findLibrary("annotations").get())
    }
}

// Plain 'java' projects (apps/adapters): only local compileOnly
plugins.withId("java") {
    if (!plugins.hasPlugin("java-library")) {
        dependencies {
            "compileOnly"(libs.findLibrary("annotations").get())
            "testCompileOnly"(libs.findLibrary("annotations").get())
        }
    }
}
