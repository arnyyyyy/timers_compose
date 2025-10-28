import java.util.Properties
import java.io.FileInputStream

plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.kotlin.compose)
        id("com.google.gms.google-services")
        id("kotlin-kapt")
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties()
if (secretsPropertiesFile.exists()) {
        FileInputStream(secretsPropertiesFile).use { secretsProperties.load(it) }
}

android {
        namespace = "com.arno.timers_compose"
        compileSdk = 36

        defaultConfig {
                applicationId = "com.arno.timers_compose"
                minSdk = 26
                targetSdk = 36
                versionCode = 1
                versionName = "1.0"

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                buildConfigField(
                        "String",
                        "GOOGLE_WEB_CLIENT_ID",
                        "\"${secretsProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "")}\""
                )
        }

        signingConfigs {
                create("release") {
                        storeFile = file("release-keystore.jks")
                        storePassword = secretsProperties.getProperty("KEYSTORE_PASSWORD")
                        keyAlias = secretsProperties.getProperty("KEY_ALIAS")
                        keyPassword = secretsProperties.getProperty("KEY_PASSWORD")
                }
        }

        buildTypes {
                release {
                        isMinifyEnabled = false
                        proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "proguard-rules.pro"
                        )
                        signingConfig = signingConfigs.getByName("release")
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
}

dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)

        implementation("androidx.navigation:navigation-compose:2.7.7")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
        implementation("androidx.compose.material:material-icons-extended:1.6.3")
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.room.common.jvm)
        implementation(libs.androidx.room.ktx)
        implementation(libs.androidx.work.runtime.ktx)
        kapt(libs.androidx.room.compiler)
        implementation("com.google.code.gson:gson:2.10.1")

        implementation("io.coil-kt:coil-compose:2.5.0")

        implementation("com.google.android.gms:play-services-auth:20.7.0")

        implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
        implementation("com.google.firebase:firebase-database-ktx")
        implementation("com.google.firebase:firebase-storage-ktx")
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.firebaseui:firebase-ui-auth:8.0.2")
        implementation("com.firebaseui:firebase-ui-database:8.0.2")

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
}