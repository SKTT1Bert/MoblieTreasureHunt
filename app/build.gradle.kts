plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"

    // Add the Compose plugin if not present
}

android {
    namespace = "com.example.treasurehunt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.treasurehunt"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // The core Compose BOM or version set
    implementation(platform("androidx.compose:compose-bom:2023.x.x"))
    // Compose BOM (manages version alignment for Compose libs)
    implementation(platform("androidx.compose:compose-bom:2023.02.00"))


    // Or if using Material3:
    implementation("androidx.compose.material3:material3")

    // Compose tooling (optional, but recommended)
    implementation("androidx.compose.ui:ui-tooling-preview")

    // If you use activity-based Compose
    implementation("androidx.activity:activity-compose:1.6.1")

    // For “rememberLauncherForActivityResult”
    implementation("androidx.compose.runtime:runtime-livedata")

    // For easy context compat calls
    implementation("androidx.core:core-ktx:1.9.0")

    // Google Play Services for location
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Core UI + Material
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.6.1")

    // If you’re using location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // If you need serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // AndroidX Core (for ContextCompat)
    implementation("androidx.core:core-ktx:1.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
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
    // If you're using the Version Catalog, you'll have lines like:
    // implementation(libs.androidx.core.ktx)
    // implementation(libs.androidx.activity.compose)
    // implementation(libs.androidx.navigation.compose)
    // etc.

    // Example usage of direct dependencies (if not using catalog for these):
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Location (Google Play services)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
}
