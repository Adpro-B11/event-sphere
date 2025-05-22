plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"
val junitJupiterVersion = "5.9.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
    testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
    testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Define functionalTest source set that also picks up src/test/java
sourceSets {
    val functionalTest by creating {
        java {
            srcDirs("src/functionalTest/java", "src/test/java")
        }
        resources {
            srcDirs("src/functionalTest/resources", "src/test/resources")
        }
        compileClasspath += sourceSets["main"].output + sourceSets["test"].output
        runtimeClasspath += output + compileClasspath
    }
}

configurations {
    // extend test dependencies to functionalTest
    named("functionalTestImplementation") {
        extendsFrom(configurations.testImplementation.get())
    }
    named("functionalTestRuntimeOnly") {
        extendsFrom(configurations.testRuntimeOnly.get())
    }
}

// Standard test task: exclude controller tests
tasks.named<Test>("test") {
    useJUnitPlatform()
    filter {
        excludeTestsMatching("*ControllerTest")
    }
    finalizedBy(tasks.named("jacocoTestReport"))
    environment("DB_URL", System.getenv("DB_URL") ?: "jdbc:h2:mem:testdb")
    environment("DB_USERNAME", System.getenv("DB_USERNAME") ?: "sa")
    environment("DB_PASSWORD", System.getenv("DB_PASSWORD") ?: "")
}

// functionalTest task: include only controller tests
tasks.register<Test>("functionalTest") {
    description = "Runs functional (Controller) tests."
    group = "verification"
    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
    useJUnitPlatform()
    filter {
        includeTestsMatching("*ControllerTest")
    }
}

// Ensure check runs both
tasks.named("check") {
    dependsOn("functionalTest")
}
