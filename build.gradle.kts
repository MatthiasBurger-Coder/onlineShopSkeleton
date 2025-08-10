import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("groovy")
    id("jacoco")
    id("info.solidsoft.pitest") version  "1.19.0-rc.1"
}

group = "de.burger.it"
version = "2.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

jacoco {
    toolVersion = "0.8.12"
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {

    // Development //

    // Spring
    implementation("org.springframework:spring-context:6.2.8")
    implementation("org.springframework:spring-core:6.2.8")
    implementation("org.springframework:spring-beans:6.2.8")
    testImplementation("org.springframework:spring-test:6.2.8")


    implementation("org.jetbrains:annotations:24.1.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")


    // Testing //

    // JUnit 5 dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.13.4")
    // The Jupiter engine provides test runtime support
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.4")

    // Gradle 9 no longer provides the JUnit Platform launcher; add it explicitly
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")

    // Spock (BDD testing with Groovy 4)
    testImplementation(platform("org.apache.groovy:groovy-bom:4.0.22"))
    testImplementation("org.apache.groovy:groovy")
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
    testImplementation("com.athaydes:spock-reports:2.5.1-groovy-4.0")

    // Mockito dependencies
    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")

    // Hamcrest for assertThat
    testImplementation("org.hamcrest:hamcrest:3.0")

    // PIT dependencies
    implementation("org.pitest:pitest:1.20.1")
    implementation("org.pitest:pitest-junit5-plugin:1.2.3")
    implementation("info.solidsoft.pitest:info.solidsoft.pitest.gradle.plugin:1.15.0")

}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // Suppress JDK warning about dynamic Java agent loading (e.g., Byte Buddy used by Mockito)
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    // Suppress CDS warning: "Sharing is only supported for bootloader classes because bootstrap classpath has been appended"
    // by disabling Class Data Sharing for the test JVM, since Mockito's Byte Buddy agent appends to the bootstrap classpath
    jvmArgs("-Xshare:off")
    finalizedBy(tasks.jacocoTestReport)

    // Use Provider-based build directory (Gradle 7+; recommended for 8/9+)
    val reportsDir = layout.buildDirectory.dir("spock-reports")

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
    // systemProperty("com.athaydes.spockframework.report.template.ReportConfiguration.showSummary", "true")
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
    jvmArgs.set(
        listOf(
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED",
            "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED"
        )
    )

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
    // You can also exclude by test names, methods, or use mutators if needed
    // mutators.set(listOf("DEFAULTS"))
}

