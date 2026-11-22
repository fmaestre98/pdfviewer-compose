plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.maven.publish)
}

// AGP 9 only registers this task on the root project, but Android Studio requests it on every
// module while syncing. An empty task keeps the sync from failing.
tasks.register("prepareKotlinBuildScriptModel")

android {
    namespace = "com.fmaestre98.pdfviewer.sdk"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    // Material3 >= 1.4 no longer brings the icons transitively.
    implementation(libs.androidx.compose.material.icons.core)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.lifecycle.viewmodel.compose)

    // Room database (private internal DB for default storage)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Gson & PDFBox
    implementation(libs.gson)
    implementation(libs.pdfbox.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

mavenPublishing {
    coordinates(
        groupId = "io.github.fmaestre98",
        artifactId = "pdfviewer-compose",
        version = providers.gradleProperty("VERSION_NAME").get()
    )

    publishToMavenCentral()
    // Keys are only provided on release machines/CI, so publishToMavenLocal works unsigned.
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    pom {
        name.set("PDF Viewer Compose")
        description.set(
            "Jetpack Compose PDF viewer for Android with text selection, highlights, " +
                "bookmarks, page notes, search and pluggable persistence."
        )
        inceptionYear.set("2026")
        url.set("https://github.com/fmaestre98/pdfviewer-compose")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("fmaestre98")
                name.set("Fabian Ortiz")
                url.set("https://github.com/fmaestre98")
            }
        }
        scm {
            url.set("https://github.com/fmaestre98/pdfviewer-compose")
            connection.set("scm:git:git://github.com/fmaestre98/pdfviewer-compose.git")
            developerConnection.set("scm:git:ssh://git@github.com/fmaestre98/pdfviewer-compose.git")
        }
    }
}
