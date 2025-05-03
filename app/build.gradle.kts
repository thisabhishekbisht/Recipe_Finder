plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.abhishek.recipefinder"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.abhishek.recipefinder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        flavorDimensions += listOf(/*"pricing",*/ "language")

        productFlavors {
            /*           create("free") {
                           applicationIdSuffix = ".free"
                           dimension = "pricing"
                       }
                       create("paid") {
                           applicationIdSuffix = ".paid"
                           dimension = "pricing"
                       }*/
            create("english") {
                dimension = "language"
            }
            create("hindi") {
                dimension = "language"
            }
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
    implementation(libs.material) // Use the latest version
    implementation(libs.material3) // ✅ Ensure this is present
    implementation(libs.gson)  // Add this line for Gson
    implementation(libs.coil.compose) // Coil for Jetpack Compose
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.android)/*Splash Screen Lib*/
    implementation(libs.androidx.core.splashscreen)/*lifecycle compose */
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.firebase.messaging)
    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    /*firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    /*authentication*/
    implementation(libs.play.services.auth) // Google Sign-In
    implementation(libs.facebook.login)
    implementation(libs.firebase.auth)
    /*Encryption*/
    implementation(libs.androidx.security.crypto)


}