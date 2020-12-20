import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version reddit.Versions.kotlin
    kotlin("kapt") version reddit.Versions.kotlin
}

repositories {
    jcenter()
}

subprojects {
    if (projectDir.name == "buildSrc") {
        return@subprojects
    }

    group = "ca.allanwang"
    version = "1.0"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")

    repositories {
        jcenter()
        maven(url = "https://dl.bintray.com/kordlib/Kord")
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(reddit.Dependencies.dagger)
        kapt(reddit.Dependencies.daggerKapt)
        implementation(reddit.Dependencies.flogger)
        implementation(reddit.Dependencies.coroutines)

        testImplementation(reddit.Dependencies.flogger("system-backend"))
        testImplementation(reddit.Dependencies.hamkrest)
        testImplementation(kotlin("test-junit5"))
        testImplementation(reddit.Dependencies.junit("api"))
        testImplementation(reddit.Dependencies.junit("params"))
        testRuntimeOnly(reddit.Dependencies.junit("engine"))

        testImplementation(reddit.Dependencies.dagger)
        kaptTest(reddit.Dependencies.daggerKapt)
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = reddit.Gradle.jvmTarget
            freeCompilerArgs = reddit.Gradle.compilerArgs
        }
    }

    if (projectDir.name in listOf("api")) {
        return@subprojects
    }

    dependencies {
        implementation(project(":api"))
    }

}