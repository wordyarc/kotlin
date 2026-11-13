// RUN_PIPELINE_TILL: CODEGEN
// DONT_TARGET_EXACT_BACKEND: JVM, NATIVE
// IGNORE_NON_REVERSED_RESOLVE: KT-62937
// IGNORE_REVERSED_RESOLVE: KT-62937
// IGNORE_PARTIAL_BODY_ANALYSIS: KT-62937
// ISSUE: KT-85639
// LANGUAGE: +ContextParameters

// ---------- Top-level classifiers ----------

class <!SLASH_IN_DECLARATION_NAME!>`Top/class`<!>
interface <!SLASH_IN_DECLARATION_NAME!>`Top/interface`<!>
object <!SLASH_IN_DECLARATION_NAME!>`Top/object`<!>
annotation class <!SLASH_IN_DECLARATION_NAME!>`Top/annotation`<!>
enum class <!SLASH_IN_DECLARATION_NAME!>`Top/enum`<!> { ENTRY }
data class <!SLASH_IN_DECLARATION_NAME!>`Top/data`<!>(val value: Int)
private class <!SLASH_IN_DECLARATION_NAME!>`Private/top`<!>

class `Normal-top`
class `Normal top`

// ---------- Nested classifiers ----------

class Outer {
    class <!SLASH_IN_DECLARATION_NAME!>`Nested/class`<!>
    inner class <!SLASH_IN_DECLARATION_NAME!>`Inner/class`<!>
    companion object <!SLASH_IN_DECLARATION_NAME!>`Companion/object`<!>

    interface <!SLASH_IN_DECLARATION_NAME!>`Nested/interface`<!>
    object <!SLASH_IN_DECLARATION_NAME!>`Nested/object`<!>
    annotation class <!SLASH_IN_DECLARATION_NAME!>`Nested/annotation`<!>
    enum class <!SLASH_IN_DECLARATION_NAME!>`Nested/enum`<!> { ENTRY }
    data class <!SLASH_IN_DECLARATION_NAME!>`Nested/data`<!>(val value: Int)
    private class <!SLASH_IN_DECLARATION_NAME!>`Private/nested`<!>

    class <!SLASH_IN_DECLARATION_NAME!>` /leading`<!>
    class <!SLASH_IN_DECLARATION_NAME!>`trailing/ `<!>
    class <!SLASH_IN_DECLARATION_NAME!>`/`<!>
    class <!SLASH_IN_DECLARATION_NAME!>`a/b/c`<!>

    class Normal {
        class <!SLASH_IN_DECLARATION_NAME!>`Deeply/nested`<!>
    }

    class `Normal-nested`
    class `Normal nested`
}

object Holder {
    class <!SLASH_IN_DECLARATION_NAME!>`InObject/class`<!>
}

interface Container {
    class <!SLASH_IN_DECLARATION_NAME!>`InInterface/class`<!>
}

enum class Enum {
    ENTRY;

    class <!SLASH_IN_DECLARATION_NAME!>`InEnum/class`<!>
}

// ---------- Members (not reported) ----------

class Members {
    val `member/property` = 1
    var `member/var` = 1
        set(`new/value`) {
            field = `new/value`
        }

    fun `member/function`(`param/name`: Int) {}

    fun <`T/member`> genericMember() {}
}

object Constants {
    const val `const/property` = 1
}

// ---------- Enum entries (not reported) ----------

enum class Letter {
    `A/a`,
    ` /leading`,
    `trailing/ `,
    `/`,
    `Normal-entry`;
}

// ---------- Type aliases (not reported) ----------

typealias `Alias/name` = String

typealias `GenericAlias/name`<`T/alias`> = List<`T/alias`>

// ---------- Type and value parameters (not reported) ----------

fun <`T/fun`> genericFun(`x/x`: `T/fun`, vararg `rest/rest`: String) {}

class GenericClass<`T/class`>(`ctor/param`: Int, val `ctor/property`: Int)

context(`c/c`: String)
fun contextParam() {}

// ---------- Local declarations (not reported) ----------

fun local() {
    class `Local/class` {
        inner class `InnerInLocal/class`
    }

    val anonymousObject = object {
        inner class `InAnonymous/class`
    }

    val `local/val` = 1
    fun `local/fun`() {}
    val lambda = { `lambda/param`: Int -> `lambda/param` }
}
