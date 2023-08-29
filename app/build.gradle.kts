import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

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
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.diaryapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
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
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Hilt-Dagger
    implementation("com.google.dagger:hilt-android:2.47")
    annotationProcessor("com.google.dagger:hilt-compiler:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
//    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    kapt("com.google.dagger:hilt-compiler:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))

    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-auth-ktx")

    //Message Bar Compose
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.5")

    //One Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.0")

    //Date-Time Picker
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // Pager - Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.27.0")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Google Auth
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    //Mongo DB Realm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt"){
        version {
            strictly("1.6.0-native-mt")
        }
    }

    implementation("io.realm.kotlin:library-base:1.10.0")

    implementation("io.realm.kotlin:library-sync:1.10.0")

    //Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    //Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")

    //Desugar JDK
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    //Coroutine lifecycle scope
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    //Corotuines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")


}