pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        jcenter()
    }
}

rootProject.name = "Conasfor"
include(":app")
 