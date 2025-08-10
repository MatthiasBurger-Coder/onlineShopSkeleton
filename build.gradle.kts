import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("jacoco")
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
}

dependencies {
    implementation("org.springframework:spring-context:6.2.8")
    implementation("org.springframework:spring-core:6.2.8")
    implementation("org.springframework:spring-beans:6.2.8")
    implementation("org.jetbrains:annotations:24.1.0")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // JUnit 5 dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    // Gradle 9 no longer provides the JUnit Platform launcher; add it explicitly
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
    // The Jupiter engine provides test runtime support
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    
    // Mockito dependencies
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.test {
    useJUnitPlatform()
    // Suppress JDK warning about dynamic Java agent loading (e.g., Byte Buddy used by Mockito)
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    // Suppress CDS warning: "Sharing is only supported for boot loader classes because bootstrap classpath has been appended"
    // by disabling Class Data Sharing for the test JVM, since Mockito's Byte Buddy agent appends to the bootstrap classpath
    jvmArgs("-Xshare:off")
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))
        csv.required.set(false)
        html.required.set(true)
    }
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
    dependsOn(tasks.test)
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}