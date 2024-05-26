plugins {
  id("java-library")
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation(libs.retrofit)
  implementation(libs.gson)
  implementation(libs.converter.gson)
  implementation(libs.okhttp)
  implementation(libs.logging.interceptor)
  implementation(libs.kotlinx.coroutines.core)
  //TEST
  testImplementation(libs.junit.v412)
  testImplementation(libs.junit.jupiter)
}

