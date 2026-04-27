plugins {
    application
    // Use the id() syntax for the JavaFX plugin
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

// Ensure the JavaFX block looks exactly like this
javafx {
    version = "21"
    // Use this assignment syntax for Kotlin DSL
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2") //driver to connect to db

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "f.App"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}