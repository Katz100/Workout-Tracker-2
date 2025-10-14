import org.gradle.kotlin.dsl.testImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.0.21"
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")

}



android {
    namespace = "com.example.chatapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatapplication"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(platform("androidx.compose:compose-bom:2025.05.00"))

    implementation("androidx.compose.ui:ui:1.9.0-alpha02")

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit.junit)

    val hilt_version = "2.51.1"

    implementation ("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    testImplementation("io.mockk:mockk:1.13.17")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

    implementation("androidx.credentials:credentials:1.5.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    kotlin("kapt")
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.6.1")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.ktor:ktor-client-websockets:3.1.2")
    implementation("io.ktor:ktor-client-cio:3.1.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}