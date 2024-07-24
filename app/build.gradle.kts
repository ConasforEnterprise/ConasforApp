plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    //kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.example.conasforapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.conasforapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-runtime:2.7.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //-------Firebase------//
    implementation("com.google.firebase:firebase-analytics")
    //implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
        //Conexión y comunicación Firebase
    implementation("com.google.firebase:firebase-firestore:25.0.0")
       //Storage o almacenamiento
    implementation("com.google.firebase:firebase-storage:20.3.0")
       //Autenticacion
    implementation("com.google.firebase:firebase-auth:22.3.1")

       //Google
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-analytics-impl:18.0.4")
    implementation("com.google.firebase:firebase-analytics:21.5.1")
    implementation("com.google.android.gms:play-services-tasks:18.0.0")

    //------Supabase-------//

    implementation("io.github.jan-tennert.supabase:postgrest-kt:0.7.6")
    implementation("io.ktor:ktor-client-cio:2.3.3")
    //implementation("io.ktor:ktor-client-cio:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")


    //-------- PiChart ----------//
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("com.github.gcacace:signature-pad:1.3.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation("androidx.fragment:fragment:1.6.2")
    implementation("com.etebarian:meow-bottom-navigation:1.0.2")

    //Imágenes
    implementation("com.squareup.picasso:picasso:2.5.2")
    implementation("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation ("com.github.AtifSayings:Animatoo:1.0.1")

    //Crashlytics
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    //------- Lottie(Animaciones) ------//
    implementation("com.airbnb.android:lottie:6.3.0") //6.3.0

    //1.0.2
    //implementation("androidx.biometric:biometric:1.2.0-alpha05")

    implementation("com.google.code.gson:gson:2.8.8")

    //implementation("androidx.paging:paging-common-ktx:3.2.1")
    //implementation("androidx.paging:paging-common-android:3.3.0-rc01")
    //implementation("androidx.paging:paging-runtime:3.2.1")

    implementation("androidx.paging:paging-runtime:3.1.1")

    // Opcional - Paging y RxJava
    implementation("androidx.paging:paging-rxjava3:3.1.1")
    //implementation("com.blogspot.atifsoftwares:animatoolib:1.0.0")
    //implementation("com.google.code.gson:gson:2.8.7")
    //implementation("com.google.firebase:firebase-appcheck:22.0.0")
    //implementation("com.google.firebase:firebase-appcheck-safety-net:22.0.0")
    //implementation("com.google.firebase:firebase-appcheck-device:22.0.0")
    //implementation("com.google.firebase:firebase-database:22.0.0")
}