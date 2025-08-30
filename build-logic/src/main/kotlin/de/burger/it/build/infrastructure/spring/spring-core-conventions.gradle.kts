@file:Suppress("UnstableApiUsage")

package de.burger.it.build.infrastructure.spring


plugins {
    `java-library`
    id("de.burger.it.build.domain.platform-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    // Pure Spring core via bundle (context/core/beans)
    "implementation"(libs.findBundle("spring").get())
    "implementation"(libs.findLibrary("spring-aop").get())
}
