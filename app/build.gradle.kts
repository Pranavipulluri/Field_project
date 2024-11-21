plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "org.meicode.fieldproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.meicode.fieldproject"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.picasso)
    implementation(libs.firebase.firestore.v2401) // Firestore dependency
    implementation(libs.recyclerview)// RecyclerView dependency
    implementation(libs.firebase.auth.v2101)
    // Firebase Authentication dependency
    implementation(libs.com.google.firebase.firebase.storage24)
    implementation(libs.firebase.firestore.v2400)
    implementation(libs.firebase.ml.vision)
    implementation(libs.glide)
    implementation(libs.recyclerview.v121)
    implementation(libs.appcompat.v161)
    implementation(libs.com.github.bumptech.glide.glide)
    annotationProcessor(libs.compiler)

    // Add this line if you're using Firebase
    implementation(libs.firebase.core)// Firebase core


}