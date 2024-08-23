plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.tinet.publish"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
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

    publishing {
        singleVariant("release")
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)

    implementation(libs.agora.rtc.basic)
    implementation(libs.agora.rtc.fullscreensharing)
    implementation(libs.agora.rtc.screencapture)

    // 视频 电子白板
    implementation(libs.fastboard.android)
    implementation(libs.gson)

    implementation(libs.glide)
    implementation(libs.swiperefreshlayout)

    implementation(libs.toaster)

    //视频客服lib层
    implementation(libs.vcsclient)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.ti-net-project"
            artifactId = "VSClientKit-Android"
            version = "0.1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}