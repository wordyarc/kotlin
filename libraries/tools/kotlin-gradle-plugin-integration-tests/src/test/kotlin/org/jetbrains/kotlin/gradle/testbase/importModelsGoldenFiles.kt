/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.testbase

import org.jetbrains.kotlin.importmodels.internal.protobuf.com.google.protobuf.Any
import org.jetbrains.kotlin.importmodels.proto.BaseModel
import org.jetbrains.kotlin.importmodels.proto.ProjectModel
import org.jetbrains.kotlin.importmodels.proto.Result
import org.jetbrains.kotlin.importmodels.proto.version
import org.jetbrains.kotlin.test.util.trimTrailingWhitespacesAndAddNewlineAtEOF
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import org.jetbrains.kotlin.importmodels.proto.error as importModelError
import org.jetbrains.kotlin.importmodels.proto.result as resultModel

private val GOLDEN_FILES_ROOT = Path.of("src/test/resources/importModels")

private fun importModelsDumpRoot(projectPath: Path): Path = projectPath.resolve("build/kotlin/import-models")

/**
 * Compares the normalized TextFormat of a dumped import model with the checked-in golden file for [projectName],
 * regenerating the golden file with the `-Pkotlin.test.update.test.data=true` flag instead of failing.
 */
internal fun assertImportModelsGolden(projectPath: Path, projectName: String, modelId: String) {
    // Gradle prints canonical paths, so the placeholder targets the project's real path
    val dumpedResult = ImportModelsTextFormat.parseResult(
        importModelsDumpRoot(projectPath).resolve("$modelId.txt").readText(),
    )
    val actual = normalizeImportModelsResultText(dumpedResult, projectPath.toRealPath().toString())
        .trimTrailingWhitespacesAndAddNewlineAtEOF()
    assertGoldenText(GOLDEN_FILES_ROOT / projectName / "$modelId.txt", actual)
}

/**
 * Compares the files the dump wrote for [projectName] with the checked-in expected listing.
 */
internal fun assertImportModelsDumpListing(projectPath: Path, projectName: String) {
    // listDirectoryEntries fails on a missing directory, so an update run cannot overwrite the listing with an empty one
    val actual = importModelsDumpRoot(projectPath)
        .listDirectoryEntries()
        .map { it.name }
        .sorted()
        .joinToString("\n").trimTrailingWhitespacesAndAddNewlineAtEOF()
    assertGoldenText(GOLDEN_FILES_ROOT / projectName / "dumpFiles.txt", actual)
}

/**
 * Compares [actual] with the checked-in golden [goldenFile], regenerating it with the
 * `-Pkotlin.test.update.test.data=true` flag instead of failing.
 */
private fun assertGoldenText(goldenFile: Path, actual: String) {
    if (System.getProperty("kotlin.test.update.test.data")?.toBoolean() == true) {
        goldenFile.parent.createDirectories()
        goldenFile.writeText(actual)
        return
    }
    if (!goldenFile.isRegularFile()) {
        throw AssertionError(
            "Missing golden file $goldenFile: run with -Pkotlin.test.update.test.data=true to generate it",
        )
    }
    // An IDE save that adds or strips the golden's final newline cannot fail the comparison
    assertEquals(goldenFile.readText().trimTrailingWhitespacesAndAddNewlineAtEOF(), actual)
}

/**
 * Normalizes a dumped `Result` for comparison as readable TextFormat: the KGP version becomes a
 * sentinel, set-like fields sort, and the project's absolute path becomes a placeholder;
 * order-significant content is left as the dump wrote it.
 */
internal fun normalizeImportModelsResultText(result: Result, projectDir: String): String {
    // The dir is replaced in its escaped form and only as a whole path component,
    // so escaped paths still match and a longer path containing the dir keeps its text
    val printedDir = Regex.escape(printedPath(projectDir))
    return ImportModelsTextFormat.printResult(normalizeResult(result))
        .replace(Regex("(?<![^\"\\s])$printedDir(?=[\"/\\\\])"), "<PROJECT_DIR>")
}

/**
 * Returns the string as the TextFormat printer writes it, escaped, by printing it as an
 * error message and reading the printed value back out.
 */
private fun printedPath(value: String): String {
    val printed = ImportModelsTextFormat.printResult(
        resultModel { error = importModelError { errorMessage = value } },
    )
    return printed.substringAfter("error_message: \"").substringBeforeLast('"')
}

private fun normalizeResult(result: Result): Result = when {
    result.resultCase != Result.ResultCase.MODEL -> result
    result.model.`is`(BaseModel::class.java) -> resultModel {
        model = Any.pack(normalizeBaseModel(result.model.unpack(BaseModel::class.java)))
    }
    result.model.`is`(ProjectModel::class.java) -> resultModel {
        model = Any.pack(normalizeProjectModel(result.model.unpack(ProjectModel::class.java)))
    }
    else -> result
}

private fun normalizeBaseModel(model: BaseModel): BaseModel {
    val builder = BaseModel.newBuilder(model)
        .clearCapabilities()
        .addAllCapabilities(model.capabilitiesList.sorted())
    // A dropped plugin version stays dropped
    if (model.hasPluginVersion()) builder.pluginVersion = version { classifier = "KGP_VERSION" }
    return builder.build()
}

private fun normalizeProjectModel(model: ProjectModel): ProjectModel = ProjectModel.newBuilder(model)
    .clearCompilationUnitIds()
    .addAllCompilationUnitIds(model.compilationUnitIdsList.sortedBy { it.value })
    .build()
