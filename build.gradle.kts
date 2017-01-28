import org.gradle.api.tasks.*
import org.gradle.api.tasks.wrapper.Wrapper

buildscript {
    extra["kotlin_version"] = "1.0.6"
    repositories {
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", extra["kotlin_version"] as String))
    }
}

apply {
    plugin("kotlin")
    plugin("idea")
}

task<Wrapper>("wrapper") {
    gradleVersion = "3.3"
}

repositories {
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib", extra["kotlin_version"] as String))
    compile(kotlinModule("reflect", extra["kotlin_version"] as String))

    compile("org.slf4j:slf4j-api:1.7.22")
    compile("ch.qos.logback:logback-classic:1.1.9")

    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.6.2")
}