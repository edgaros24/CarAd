pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.google.com") }

    }
    extra["compose_version"] = "1.0.5"
    extra["firebase_auth_version"] = "23.1.0"
}

rootProject.name = "CarAd"
include(":app")
 