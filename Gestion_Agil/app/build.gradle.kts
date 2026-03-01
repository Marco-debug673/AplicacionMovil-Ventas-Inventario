@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.gestion_agil"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.gestion_agil"
        minSdk = 30
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 7
        versionName = "1.1.1"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    //noinspection UseTomlInstead
    implementation("androidx.core:core-splashscreen:1.2.0")
    //noinspection UseTomlInstead
    implementation("org.mindrot:jbcrypt:0.4")
    // --- WorkManager ---
    //noinspection UseTomlInstead
    implementation("androidx.work:work-runtime-ktx:2.11.1")
    // --- ROOM ---
    //noinspection UseTomlInstead
    implementation("androidx.room:room-runtime:2.8.4")
    implementation(libs.androidx.core.i18n)
    //noinspection UseTomlInstead,KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.8.4")
    //noinspection UseTomlInstead
    implementation("androidx.room:room-ktx:2.8.4")
    //LiveData y ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // --- Coroutines ---
    //noinspection UseTomlInstead
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    //noinspection UseTomlInstead
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    //Fragments
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}