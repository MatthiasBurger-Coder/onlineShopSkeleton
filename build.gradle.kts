plugins {
    application
    id("de.burger.it.build.application.spring-app")
    id("de.burger.it.build.application.lombok-app")
    id("de.burger.it.build.application.jetbrains-annotations-app")
    id("de.burger.it.build.infrastructure.spring.spring-test-conventions")
    id("java")
    id("groovy")
    id("jacoco")
    id("pmd")
    alias(libs.plugins.pitest)
}

group = "de.burger.it"
version = "2.0-SNAPSHOT"

application {
    // Main entry point for console run
    mainClass.set("de.burger.it.Main")
}

dependencies {
    // --- JUnit 5 ---
    testImplementation(libs.bundles.junitApi)
    testRuntimeOnly(libs.bundles.junitRt)

    // --- Spock (with Groovy BOM as platform) ---
    testImplementation(platform(libs.groovy.bom)) // BOM must be added as platform
    testImplementation(libs.bundles.spock)

    // --- Mockito / Hamcrest ---
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.hamcrest)

    // --- Logging (SLF4J API + binding) ---
    // Provided via version catalog bundle (SLF4J 2.0.x API + Logback 1.5.x).
    implementation(libs.bundles.logging)

    // --- pitest ---
    testImplementation(libs.bundles.pitest)
}

tasks.test {
    useJUnitPlatform()
}

// Resolve Mockito agent jar path from the test runtime classpath lazily
val mockitoAgentJar: Provider<String> = configurations.named("testRuntimeClasspath").map { cfg ->
    cfg.files.firstOrNull { it.name.startsWith("mockito-core") && it.name.endsWith(".jar") }?.absolutePath ?: ""
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // Prefer passing Mockito as a javaagent instead of relying on self-attachment (future-JDK-safe)
    val agentPath = mockitoAgentJar.get()
    if (agentPath.isNotBlank()) {
        jvmArgs("-javaagent:$agentPath")
    }

    // Disabling CDS avoids noisy warnings when agents append to bootstrap classpath on some JDKs
    jvmArgs("-Xshare:off")
    finalizedBy(tasks.jacocoTestReport)

    // Use a Provider-based build directory (Gradle 7+; recommended for 8/9+)
    val reportsDir = layout.buildDirectory.dir("reports/spock")

    // Pass absolute path lazily to the test JVM
    systemProperty(
        "com.athaydes.spockframework.report.outputDir",
        reportsDir.map { it.asFile.absolutePath }.get()
    )

    // Optional extras
    systemProperty("com.athaydes.spockframework.report.projectName", "Customer Service Specs")
    systemProperty("com.athaydes.spockframework.report.projectVersion", "2.0-SNAPSHOT")
    systemProperty("com.athaydes.spockframework.report.outputFormats", "html")
    systemProperty("com.athaydes.spockframework.report.showCodeBlocks", "true")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))
        csv.required.set(false)
        html.required.set(true)
    }
    // Exclude the application entry point from coverage reports
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("de/burger/it/Main*")
                }
            }
        )
    )
}
tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    // Fail the build if coverage is below 86%
    violationRules {
        rule {
            limit {
                // enforce 86% line coverage
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.86".toBigDecimal()
            }
        }
    }
    // Exclude the application entry point from coverage verification
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("de/burger/it/Main*")
                }
            }
        )
    )
    dependsOn(tasks.test)
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}

// PIT - Tests
configurations.named("pitest") {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.pitest" && requested.name == "pitest-command-line") {
            useTarget("org.pitest:pitest-command-line:1.20.1")
            because("RC artifact is not published to Maven Central; use stable command-line")
        }
    }
}
pitest {
    // Align PIT core with the forced command-line above
    pitestVersion.set("1.20.1")

    // --- JDK 21 module openness fixes (Mockito/ByteBuddy/etc.) ---
    // and attach Mockito as a Java agent to avoid self-attach warnings in future JDKs
    val pitJvmArgs = mutableListOf(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED"
    )
    val pitAgentPath = mockitoAgentJar.get()
    if (pitAgentPath.isNotBlank()) {
        pitJvmArgs += listOf("-javaagent:$pitAgentPath")
    }
    jvmArgs.set(pitJvmArgs)

    // --- diagnostics ---
    verbose.set(true)                     // More PIT logs
    useClasspathFile.set(true)            // Fix long classpath on Windows
    timestampedReports.set(true)

    // --- Scope of mutation testing (adapt to your packages) ---
    targetClasses.set(listOf("de.burger.it.*"))
    targetTests.set(listOf("de.burger.it.*Test", "de.burger.it.*IT"))
    failWhenNoMutations.set(false)         // fail fast if nothing matched (helps diagnose)

    // --- Runtime / reporting ---
    threads.set(4)
    outputFormats.set(listOf("HTML"))
    exportLineCoverage.set(true)
    timestampedReports.set(true)
    mutationThreshold.set(80)

    // --- Useful filters (speed + signal) ---
    excludedClasses.set(
        listOf(
            "de.burger.it.domain.*",
            "de.burger.it.Main*"
        )
    )
}
