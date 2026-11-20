/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.benchmarks.collections

import kotlinx.benchmark.*

@State(Scope.Benchmark)
abstract class BaseIterableToListBenchmark {
    @Param(
        "ArrayList",
        // Name rationale: it implements to `List` but doesn't inherit `AbstractCollection` nor implements `RandomAccess`
        "ListWorstCase",
        "HashSet",
    )
    var sourceKind: String = ""

    @Param("0", "1", "2", "10", "100", "1000", "10000", "100000")
    var size: Int = 0

    private lateinit var sut: Iterable<String>

    @Setup
    fun setUp() {
        sut = when (sourceKind) {
            "ArrayList" -> sizedArrayList(size)
            "ListWorstCase" -> object : List<String> by sizedArrayList(size) {}
            "HashSet" -> HashSet(sizedArrayList(size))
            else -> throw IllegalStateException("Unknown source kind: $sourceKind")
        }
    }

    @Benchmark
    fun toList(blackhole: Blackhole) {
        blackhole.consume(sut.toList())
    }

    private fun sizedArrayList(count: Int) = ArrayList<String>().apply {
        repeat(count) { index ->
            add(index.toString())
        }
    }
}
