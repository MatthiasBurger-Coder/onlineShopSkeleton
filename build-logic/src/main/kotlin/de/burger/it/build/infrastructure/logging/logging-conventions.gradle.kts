@file:Suppress("UnstableApiUsage")


package de.burger.it.build.infrastructure.logging
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Expose the SLF4J facade to downstream modules
    api(libs.findLibrary("slf4j-api").get())

    // Route legacy frameworks to SLF4J (enable if you actually use JUL/JCL)
    implementation(libs.findLibrary("jul-to-slf4j").get())
    implementation(libs.findLibrary("jcl-over-slf4j").get())

    // Log4j2 backend + single SLF4J binding at runtime
    runtimeOnly(libs.findLibrary("log4j-api").get())
    runtimeOnly(libs.findLibrary("log4j-core").get())
    runtimeOnly(libs.findLibrary("log4j-slf4j2-impl").get())

    // Optional: bridge for legacy org.apache.log4j.* usage (remove after migration)
    // runtimeOnly(libs.findLibrary("log4j-12-api").get())

    // Optional: align Log4j versions via BOM (must be added with platform, not as a plain dependency)
    implementation(platform(libs.findLibrary("log4j-bom").get()))

    // Optional: async logging support (only if you use AsyncLoggers)
    runtimeOnly(libs.findLibrary("disruptor").get())

    // Tests: compile against Log4j types; ListAppender lives in the tests classifier
    testImplementation(libs.findLibrary("log4j-api").get())
    testImplementation(libs.findLibrary("log4j-core-test").get())
    testRuntimeOnly(libs.findLibrary("log4j-slf4j2-impl").get())

    // AOP: Spring AOP + AspectJ runtime (weaver only for LTW)
    //implementation(libs.findLibrary("spring-aop").get())
    implementation(libs.findLibrary("aspectj-rt").get())
    runtimeOnly(libs.findLibrary("aspectj-weaver").get()) // only if you enable LTW
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

extensions.configure<JavaPluginExtension> {
    // Keep consistent dependency resolution across compile/runtime
    @Suppress("UnstableApiUsage")
    consistentResolution {
        useCompileClasspathVersions()
    }
}