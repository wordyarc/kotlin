import org.gradle.api.Project
import java.io.File

plugins {
    id("common-configuration")
    id("com.autonomousapps.dependency-analysis")
}

project.removePrePushHookIfExists()

fun Project.removePrePushHookIfExists() {
    val prePushHookPath = rootProject.getGitDirectory().toPath()
        .resolve("hooks")
        .resolve("pre-push")
    java.nio.file.Files.deleteIfExists(prePushHookPath)
}

fun Project.getGitDirectory(): File {
    val dotGitFile = File(projectDir, ".git")

    return if (dotGitFile.isFile) {
        val workTreeLink = dotGitFile.readLines().single { it.startsWith("gitdir: ") }
        val gitDirPath = workTreeLink.substringAfter("gitdir: ")
        val gitDirectory = File(gitDirPath).takeIf { it.isAbsolute } ?: File(projectDir, gitDirPath)

        val commonDirFile = File(gitDirectory, "commondir")
        if (commonDirFile.isFile) {
            File(gitDirectory, commonDirFile.readLines().first()).also { require(it.isDirectory) }
        } else {
            gitDirectory
        }
    } else {
        dotGitFile
    }
}
