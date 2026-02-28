plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0"

application {
    mainClass.set("org.example.Main")
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
}

tasks.test {
    useJUnitPlatform()
}