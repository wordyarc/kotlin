/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics
import org.jetbrains.kotlin.gradle.testbase.GradleTest
import org.jetbrains.kotlin.gradle.testbase.ImportModelsTextFormat
import org.jetbrains.kotlin.gradle.testbase.JvmGradlePluginTests
import org.jetbrains.kotlin.gradle.testbase.KGPBaseTest
import org.jetbrains.kotlin.gradle.testbase.assertHasDiagnostic
import org.jetbrains.kotlin.gradle.testbase.assertImportModelsDumpListing
import org.jetbrains.kotlin.gradle.testbase.assertImportModelsGolden
import org.jetbrains.kotlin.gradle.testbase.assertTasksExecuted
import org.jetbrains.kotlin.gradle.testbase.build
import org.jetbrains.kotlin.gradle.testbase.project
import org.jetbrains.kotlin.importmodels.KotlinImportModelIds
import org.jetbrains.kotlin.importmodels.proto.Result
import java.nio.file.Files
import kotlin.io.path.readBytes
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private const val DUMP_TASK = "dumpKotlinImportModels"
private const val DUMP_TASK_ARGUMENT = "-Pkotlin.internal.dumpKotlinImportModels=true"

@JvmGradlePluginTests
class KotlinImportModelsDumpIT : KGPBaseTest() {
    @GradleTest
    fun `dump writes golden-comparable models without compiling`(gradleVersion: GradleVersion) {
        project(projectName = "simpleProject", gradleVersion = gradleVersion) {
            build(DUMP_TASK, DUMP_TASK_ARGUMENT) {
                assertTasksExecuted(":$DUMP_TASK")
                // The dump needs nothing else built
                assertEquals(setOf(":$DUMP_TASK"), tasks.map { it.path }.toSet())
                assertHasDiagnostic(
                    KotlinToolingDiagnostics.InternalKotlinGradlePluginPropertiesUsed,
                    "kotlin.internal.dumpKotlinImportModels",
                )
            }

            val dumpRoot = projectPath.resolve("build/kotlin/import-models")
            assertImportModelsDumpListing(projectPath, "simpleProject")

            val baseBytes = dumpRoot.resolve("base.bin").readBytes()
            assertImportModelsGolden(projectPath, "simpleProject", KotlinImportModelIds.BASE)
            assertImportModelsGolden(projectPath, "simpleProject", KotlinImportModelIds.PROJECT_INFORMATION)

            // Every readable dump re-parses to the same message as its bytes and re-prints to the
            // same expanded text: a corrupted or unexpanded text leg cannot pass the goldens above
            dumpRoot.toFile().listFiles().orEmpty()
                .filter { it.name.endsWith(".txt") }
                .forEach { textFile ->
                    val text = textFile.readText()
                    val reparsed = ImportModelsTextFormat.parseResult(text)
                    assertEquals(
                        Result.parseFrom(
                            dumpRoot.resolve("${textFile.name.removeSuffix(".txt")}.bin").readBytes(),
                        ),
                        reparsed,
                    )
                    assertEquals(ImportModelsTextFormat.printResult(reparsed), text)
                }

            val staleFile = dumpRoot.resolve("unexpected.txt")
            staleFile.writeText("stale")
            build(DUMP_TASK, DUMP_TASK_ARGUMENT) { assertTasksExecuted(":$DUMP_TASK") }
            assertFalse(Files.exists(staleFile))
            assertContentEquals(baseBytes, dumpRoot.resolve("base.bin").readBytes())
        }
    }
}
