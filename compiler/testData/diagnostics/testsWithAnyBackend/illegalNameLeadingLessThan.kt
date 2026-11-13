// RUN_PIPELINE_TILL: CODEGEN
// DONT_TARGET_EXACT_BACKEND: JVM, NATIVE
// IGNORE_NON_REVERSED_RESOLVE: KT-62937
// IGNORE_REVERSED_RESOLVE: KT-62937
// IGNORE_PARTIAL_BODY_ANALYSIS: KT-62937
// ISSUE: KT-85639
// LANGUAGE: +ContextParameters

// ---------- Classifiers (not reported) ----------

class `<C`(`not<Leading`: Int)

interface `<I`

object `<O`

annotation class `<A`

enum class `<E` { ENTRY }

class Outer {
    class `<Nested`
    inner class `<Inner`
    companion object `<Companion`
}

// ---------- Enum entries ----------

enum class Entries {
    <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<startingEntry`,<!>
    `middle<Entry`,
    normalEntry;
}

// ---------- Functions ----------

fun <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<topLevelFun`<!>() {}

fun String.<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<extensionFun`<!>() {}

private fun <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<privateFun`<!>() {}

fun `middle<Fun`() {}

fun `trailing<`() {}

fun `>notLessThan`() {}

fun ` <leadingSpace`() {}

class Members {
    fun <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<memberFun`<!>() {}
}

// ---------- Properties ----------

val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<topLevelVal`<!> = 1

var <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<topLevelVar`<!> = 1

val String.<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<extensionProperty`<!>: Int
    get() = 1

private val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<privateVal`<!> = 1

val `middle<Val` = 1

object Constants {
    const val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<const`<!> = 1
}

class PropertyMembers {
    val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<memberVal`<!> = 1
    var <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<memberVar`<!> = 1
        set(<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<newValue`<!>) {
            field = `<newValue`
        }
}

// ---------- Overrides ----------

interface Base {
    fun <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<baseFun`<!>()
    val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<baseVal`<!>: Int
}

class Derived : Base {
    override fun <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<baseFun`<!>() {}
    override val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<baseVal`<!>: Int = 1
}

// Fake overrides are not reported.
class <!LEADING_LESS_THAN_IN_DECLARATION_NAME, LEADING_LESS_THAN_IN_DECLARATION_NAME!>DerivedFromDerived<!> : Base by Derived()

// ---------- Type aliases ----------

typealias <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<Alias`<!> = String

typealias <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<GenericAlias`<!><T> = List<T>

typealias `middle<Alias` = String

// ---------- Value parameters ----------

fun params(<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<param`: Int<!>, `middle<Param`: Int, <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>vararg `<vararg`: String<!>) {}

fun defaultParam(<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<default`: Int = 1<!>) {}

class CtorParams(<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<ctorParam`: Int<!>, <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>val <!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<ctorProperty`<!>: Int<!>)

context(<!LEADING_LESS_THAN_IN_DECLARATION_NAME!>`<context`: String<!>)
fun contextParam() {}

// ---------- Type parameters (not reported) ----------

fun <`<T`> genericFun(x: `<T`) {}

class GenericClass<`<T`>

inline fun <reified `<R`> reifiedFun() {}

// ---------- Local declarations (not reported) ----------

fun local() {
    val `<localVal` = 1
    var `<localVar` = 2
    fun `<localFun`(`<localFunParam`: Int) {}
    class `<LocalClass`(`<localCtorParam`: Int) {
        fun `<localClassMember`() {}
        val `<localClassProperty` = 1
    }

    val (`<d1`, `<d2`) = Pair(1, 2)

    for (`<loopVar` in 1..2) {}

    try {} catch (`<e`: Throwable) {}

    val lambda = { `<lambdaParam`: Int -> `<lambdaParam` }
    val anonymous = fun(`<anonParam`: Int) = `<anonParam`

    val anonymousObject = object {
        val `<objMember` = 1
        fun `<objFun`() {}
    }

    val `<delegated` by lazy { 1 }
}
