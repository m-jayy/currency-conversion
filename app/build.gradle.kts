import com.google.gson.JsonParser

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mohsin.mycurrencyconverterapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mohsin.mycurrencyconverterapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load secrets from env.secrets.json and add as build config fields
        val secrets = getEnvSecrets()
        if (secrets != null) {
            buildConfigField("String", "BASE_URL", "\"${secrets.get("BASE_URL").asString}\"")
            buildConfigField("String", "APP_ID", "\"${secrets.get("APP_ID").asString}\"")
        } else {
            throw GradleException("env.secrets.json file not found!")
        }
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
        buildConfig = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

}

// Function to parse env.secrets.json
fun getEnvSecrets(): com.google.gson.JsonObject? {
    val secretsFile = file("env.secrets.json")
    return if (secretsFile.exists()) {
        JsonParser.parseString(secretsFile.readText()).asJsonObject
    } else {
        null
    }
}

dependencies {
    // Core Android Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

// Local Storage Libraries
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore)

// Jetpack Compose Libraries
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

// Dependency Injection Libraries
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

// Networking Libraries
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

// Testing Libraries
// Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.cash.turbine)

// Instrumentation Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

// Debugging Libraries
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}