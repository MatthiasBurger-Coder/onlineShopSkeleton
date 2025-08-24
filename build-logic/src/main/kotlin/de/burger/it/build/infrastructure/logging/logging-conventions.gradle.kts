@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.logging

plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Use SLF4J facade for API surface
    "api"(libs.findBundle("loggingApi").get())

    // Bring Log4j2 backend + the single SLF4J binding at runtime
    "runtimeOnly"(libs.findBundle("loggingRuntime").get())

    "testImplementation"(libs.findBundle("loggingTesting").get())
    "testImplementation"(libs.findBundle("loggingBridges").get())

    // Route legacy JUL/JCL logs into SLF4J (enable if your app uses them)
    "implementation"(libs.findBundle("loggingBridges").get())
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
val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer
sourceSets.named("main") {
    // Provide shared logging config to every module at runtime
    resources.srcDir(rootProject.layout.projectDirectory.dir("config/logging"))
    // Do not ship the test variant with main
    resources.exclude("log4j2-test.xml")
}
sourceSets.named("test") {
    // Allow tests to prefer log4j2-test.xml automatically
    resources.srcDir(rootProject.layout.projectDirectory.dir("config/logging"))
}

extensions.configure<JavaPluginExtension> {
    // Keep consistent dependency resolution across compile/runtime
    @Suppress("UnstableApiUsage")
    consistentResolution {
        useCompileClasspathVersions()
    }
}