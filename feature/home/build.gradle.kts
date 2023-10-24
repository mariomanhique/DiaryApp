@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.mariomanhique.home"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    

    implementation(libs.material3.compose)
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling.preview)
    implementation(libs.ui.tooling.preview.android)

    implementation(libs.coil)
    implementation(libs.google.fonts)

    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.coroutines.core)

    implementation(libs.hilt.android)
//    ksp("com.google.dagger:hilt-android-compiler:2.48")
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.firebase.auth)
    implementation(libs.realm.sync)

    implementation(libs.date.time.picker)
    implementation(libs.date.dialog)
    implementation(libs.time.dialog)


    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:database"))
    implementation(project(":feature:auth"))
    implementation(project(":data:firestore"))
}