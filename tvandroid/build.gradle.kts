plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Leanback — el estándar de Android TV con Views/Fragments
    implementation("androidx.leanback:leanback:1.2.0")

    // Glide para cargar imágenes en las cards
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Compartir Room + Repository con el módulo app
    implementation(project(":shared"))

    // ViewModel para observar datos con StateFlow
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Fragment KTX — necesario para el delegado "by viewModels()"
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    implementation("androidx.room:room-runtime:2.6.1")

    // Media3 + ExoPlayer
    val media3Version = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")

    //Broker
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}