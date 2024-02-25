plugins {
    id("java")
}

group = "cn.pigeon"
version = "3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Annotation"))
    compileOnly("libs:tools")
    implementation("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}
