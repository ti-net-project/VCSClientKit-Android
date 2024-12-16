plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

object PublicSdkConfig{
    const val versionName = "0.3.3"
}

android {
    namespace = "com.tinet.vcskit"
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release")
    }
}

dependencies {
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.cardview:cardview:1.0.0")

    val agoraSdkVersion = "4.3.2"
    implementation("io.agora.rtc:full-rtc-basic:${agoraSdkVersion}")
    implementation("io.agora.rtc:full-screen-sharing:${agoraSdkVersion}")
    implementation("io.agora.rtc:screen-capture:${agoraSdkVersion}")

    implementation("com.github.netless-io:fastboard-android:1.3.4")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.github.getActivity:Toaster:12.6")

    implementation("com.github.ti-net-project:VCSClient-Android:0.3.0")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.ti-net-project"
            artifactId = "VCSClientKit-Android"
            version = PublicSdkConfig.versionName

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}


val version = PublicSdkConfig.versionName
val sdkFile = "VCSClientKit_${PublicSdkConfig.versionName}_release.zip"
val zipPackagesPath = File("${rootDir.absolutePath}/zip_packages/$sdkFile")

task<Delete>("deleteExpiredFiles"){
    dependsOn("clean")

    delete = setOf(
        "${projectDir.path}/libs",
        "${projectDir.path}/src/main/jinLibs",
        "${projectDir.path}/src/main/res",
        "${projectDir.path}/src/main/AndroidManifest.xml",
        "${projectDir.path}/proguard-rules.pro",
        "${projectDir.path}/consumer-rules.pro"
    )

    doLast {
        println("start to copy libs")

        copy {
            from(zipTree(zipPackagesPath))
            include("arm*/**","x86*/**")
            into("${project.projectDir}/src/main/jniLibs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("*.jar")
            into("${project.projectDir}/libs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("include/**")
            into("${project.projectDir}/src/main/jniLibs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("res/**")
            into("${project.projectDir}/src/main")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("AndroidManifest.xml")
            into("${project.projectDir}/src/main")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("proguard-rules.pro")
            into("${project.projectDir}")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("consumer-rules.pro")
            into("${project.projectDir}")
        }
    }
}

tasks.named("preBuild"){
    dependsOn("deleteExpiredFiles")
}