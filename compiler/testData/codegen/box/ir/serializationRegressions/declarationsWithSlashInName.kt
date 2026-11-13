// ISSUE: KT-85639
// DONT_TARGET_EXACT_BACKEND: JVM, NATIVE
// WITH_STDLIB
// A slash in a class name breaks klib linkage: ClassId uses the slash as a package separator, so such classes
// cannot be resolved from dependent modules. They are reported by SLASH_IN_DECLARATION_NAME.
// Functions, properties and enum entries with slashes link fine, but the KLIB ABI reader cannot read their
// qualified names, which breaks the ABI dump made by the test infrastructure, so they aren't tested here.
// Local declarations, type aliases, type and value parameters may have slashes.
// JsAllowInvalidCharsIdentifiersEscaping lets top-level declarations use characters invalid in JS identifiers.

// MODULE: lib
// FILE: lib.kt
package lib

// Top-level declarations with characters invalid in JS identifiers, other than a slash
fun ` public `(): String = "public"

fun `dash-function`(): String = "dash"

fun `plus+function`(): String = "plus"

val `top-level property`: String = "top"

var `top-level var`: String = ""

fun String.`extension-function`(): String = this + "!"

val String.`extension property`: String
    get() = this + "?"

class `Space Class`(val value: String)

object `Dash-Object` {
    val value: String = "object"
}

interface `Interface-Name` {
    fun get(): String
}

class `Impl-Class` : `Interface-Name` {
    override fun get(): String = "impl"
}

enum class `Enum-Class` {
    `entry one`,
}

typealias ` /Alias/ ` = `Space Class`

// Type aliases
class Real(val v: String)

typealias `Real/alias` = Real

typealias `List/alias`<T> = List<T>

fun makeAlias(v: String): `Real/alias` = Real(v)

fun aliasValue(x: `Real/alias`): String = x.v

fun makeListAlias(): `List/alias`<String> = listOf("a", "b")

// Type parameters
fun <`T/p`> identity(x: `T/p`): `T/p` = x

class Container<`T/p`>(val value: `T/p`) {
    fun <`R/r`> map(f: (`T/p`) -> `R/r`): Container<`R/r`> = Container(f(value))
}

inline fun <reified `T/reified`> isInstance(x: Any): Boolean = x is `T/reified`

// Value parameters
fun concat(`first/param`: String, `second/param`: String = "default"): String = `first/param` + `second/param`

class WithParams(`first/ctor/param`: String, `second/ctor/param`: String) {
    val combined: String = `first/ctor/param` + `second/ctor/param`
}

// MODULE: main(lib)
// FILE: main.kt
package main

import lib.*

fun box(): String {
    // Top-level declarations with characters invalid in JS identifiers, other than a slash
    if (` public `() != "public") return "Fail function with spaces"
    if (`dash-function`() != "dash") return "Fail function with dash"
    if (`plus+function`() != "plus") return "Fail function with plus"
    if (`top-level property` != "top") return "Fail top-level property: ${`top-level property`}"
    `top-level var` = "set"
    if (`top-level var` != "set") return "Fail top-level var: ${`top-level var`}"
    if ("a".`extension-function`() != "a!") return "Fail extension function"
    if ("a".`extension property` != "a?") return "Fail extension property"
    if (`Space Class`("space").value != "space") return "Fail class with space"
    if (`Dash-Object`.value != "object") return "Fail object with dash"
    val impl: `Interface-Name` = `Impl-Class`()
    if (impl.get() != "impl") return "Fail interface implementation"
    if (`Enum-Class`.entries.map { it.name } != listOf("entry one")) return "Fail enum: ${`Enum-Class`.entries}"
    val aliased: ` /Alias/ ` = ` /Alias/ `("alias")
    if (aliased.value != "alias") return "Fail type alias with slashes"

    // Type aliases
    val alias: `Real/alias` = `Real/alias`("alias")
    if (alias.v != "alias") return "Fail type alias: ${alias.v}"
    val list: `List/alias`<Int> = listOf(1, 2)
    if (list.size != 2) return "Fail generic type alias: $list"
    if (aliasValue(makeAlias("sig")) != "sig") return "Fail type alias in signatures"
    if (makeListAlias() != listOf("a", "b")) return "Fail generic type alias in signatures: ${makeListAlias()}"

    // Type parameters
    if (identity("id") != "id") return "Fail generic function"
    if (Container(21).map { it * 2 }.value != 42) return "Fail generic class"
    if (!isInstance<String>("s") || isInstance<Int>("s")) return "Fail reified type parameter"

    // Value parameters
    if (concat(`second/param` = "B", `first/param` = "A") != "AB") return "Fail named value parameters"
    if (concat("A") != "Adefault") return "Fail default value parameter"
    val withParams = WithParams(`second/ctor/param` = "2", `first/ctor/param` = "1")
    if (withParams.combined != "12") return "Fail constructor parameters: ${withParams.combined}"

    // Local declarations
    fun `local/function`(): String = "local"
    class `Local/class`(val value: String)
    if (`Local/class`(`local/function`()).value != "local") return "Fail local class and function"
    val (`destructured/first`, `destructured/second`) = Pair(1, 2)
    if (`destructured/first` + `destructured/second` != 3) return "Fail destructuring"
    val `local/val` = "O"
    val lambda = { `lambda/param`: String -> `lambda/param` + "K" }
    return lambda(`local/val`)
}
