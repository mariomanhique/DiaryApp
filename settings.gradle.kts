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
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "DiaryApp"
include(":app")
include(":core:ui")
include(":core:util")
include(":data:firestore")
include(":data:database")
include(":feature:auth")
include(":feature:home")
include(":feature:write")
//include(":feature:feed")
include(":feature:profile")
//include(":feature:connect")
include(":core:datastore")
