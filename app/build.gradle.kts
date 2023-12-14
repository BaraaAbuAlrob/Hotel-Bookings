plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.finalandroid.project.hotelbooking"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.finalandroid.project.hotelbooking"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    //material design
    api ("com.google.android.material:material:1.1.0-alpha06")

    //CardView
    implementation("androidx.cardview:cardview:1.0.0")

//    //RecyclerView
//    implementation("androidx.recyclerview:recyclerview:1.4.0-alpha01")
//    // For control over item selection of both touch and mouse driven selection
//    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.5.2")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.7.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.7.2")

    //Interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:3.4.1")

    //Firebase
    implementation("com.google.firebase:firebase-storage:20.3.0")

    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}