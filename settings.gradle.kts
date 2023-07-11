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
    }
}
rootProject.name = "Ufc tracker"
include(":app")
include(":core:data")
include(":core:domain")
include(":feature:events")
include(":core:common")
include(":core:model")
include(":core:designsystem")
include(":feature:fightbets")
include(":core:database")
