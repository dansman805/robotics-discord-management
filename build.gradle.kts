import org.jetbrains.kotlin.base.kapt3.KaptOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM
    id("org.jetbrains.kotlin.jvm").version("1.3.71")
    kotlin("plugin.serialization") version "1.3.70"

    // Apply the application to add support for building a CLI application
    application
    // kotlin("jvm") version "1.3.71"
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.

    jcenter()

    maven { setUrl("https://jitpack.io") }
}

dependencies {
    // Use the Kotlin JDK 8 standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation("me.jakejmattson:KUtils:0.17.1")

    val fuelVersion = "2.2.0"

    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-kotlinx-serialization:$fuelVersion")

    implementation("org.xerial:sqlite-jdbc:3.30.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("org.jsoup:jsoup:1.10.3")

    implementation("me.liuwj.ktorm:ktorm-core:2.7.2")

    implementation("org.knowm.xchart:xchart:3.6.2")

    implementation("com.sksamuel.scrimage:scrimage-core:4.0.4")
}

application {
    // Define the main class for the application
    mainClassName = "com.github.dansman805.discordbot.AppKt"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
