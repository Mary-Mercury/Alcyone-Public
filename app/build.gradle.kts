//import org.jetbrains.kotlin.konan.properties.Properties
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.8.21"
}


android {
    namespace = "com.example.alcyone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.alcyone"
        minSdk = 26
        targetSdk = 34
        versionCode = 6
        versionName = "1.2.5"

        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "API_KEY", localProperties.getProperty("apiKey"))
        buildConfigField("String", "URL", localProperties.getProperty("baseUrl"))


    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        buildConfig = true
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.android.datatransport:transport-runtime:3.3.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("com.google.android.material:material:1.11.0")


    //имплиментации хилта
    implementation("com.google.dagger:hilt-android:2.47")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.5")
    kapt("com.google.dagger:hilt-android-compiler:2.47")

    //имплиментации supabase
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.2.3")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.2.3")



    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    implementation ("com.facebook.shimmer:shimmer:0.5.0@aar")

    implementation ("androidx.preference:preference-ktx:1.2.1")

    implementation ("com.michalsvec:single-row-calednar:1.0.0")

    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.compose.runtime:runtime:1.6.5")

    //доп имплы для внедрения авторизации
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation ("androidx.activity:activity-compose:1.8.2")
//    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.2.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.6.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.5")
    implementation("androidx.transition:transition:1.2.0@aar")
    implementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.compose.material:material-icons-extended:1.6.5")
}