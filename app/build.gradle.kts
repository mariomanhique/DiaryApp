//import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.realm.kotlin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.diaryapp"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.diaryapp"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

//    kapt {
//        correctErrorTypes = true
//    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.material3.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Hilt-Dagger
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)
//    kapt("com.google.dagger:hilt-android-compiler:2.47")
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(platform(libs.firebase))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)



    //Message Bar Compose
    implementation(libs.message.bar.compose)

    //One Tap Compose

    // Pager - Accompanist
    implementation(libs.accompanist.pager)
    //Coil
    implementation(libs.coil)

    //Google Auth
    implementation(libs.play.services.auth)

    //Mongo DB Realm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt"){
        version {
            strictly("1.6.0-native-mt")
        }
    }
    implementation(libs.realm.sync)
    //Splash API
    implementation(libs.splash.api)
    //Compose Navigation
    implementation(libs.navigation.compose)
    //Desugar JDK
    coreLibraryDesugaring(libs.desugar.jdk)
    //Coroutine lifecycle scope
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.lifecycle.runtime)
    implementation (libs.lifecycle.runtime.compose)
    //Fonts
    implementation(libs.google.fonts)
    // Date-Time Picker
    implementation (libs.date.time.picker)
    // CALENDAR
    implementation (libs.date.dialog)
    // CLOCK
    implementation (libs.time.dialog)
    //Corotuines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:database"))
    implementation(project(":data:firestore"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))

}