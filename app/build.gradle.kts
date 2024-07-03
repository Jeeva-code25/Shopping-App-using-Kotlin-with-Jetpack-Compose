plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.narumanam"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.narumanam"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Icons
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    //SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")
    //SystemUI
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    //Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database:20.3.0")

    //Pick image from Gallery
    implementation("io.coil-kt:coil-compose:2.5.0")

    //Firebase Storage
    implementation("com.google.firebase:firebase-storage:20.3.0")

    //ImageLoad
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")

    //Write PDF
    implementation ("com.itextpdf:itextg:5.5.10")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}