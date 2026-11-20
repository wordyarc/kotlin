/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.ring

import kotlinx.benchmark.*
import org.jetbrains.kotlin.benchmarks.collections.BaseIterableToListBenchmark

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Measurement(time = 100, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
open class IterableToListBenchmark : BaseIterableToListBenchmark()
