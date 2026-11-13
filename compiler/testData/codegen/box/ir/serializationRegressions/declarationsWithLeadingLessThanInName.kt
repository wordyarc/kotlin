// ISSUE: KT-85639
// DONT_TARGET_EXACT_BACKEND: JVM, NATIVE
// WITH_STDLIB

// MODULE: lib
// FILE: lib.kt
package lib

// IrClass: classes
class `<startingClass`(val value: String)
class `middle<Class`(val value: String)
class normalClass(val value: String)

// IrClass: interfaces
interface `<startingInterface` {
    fun get(): String
}
interface `middle<Interface` {
    fun get(): String
}
interface normalInterface {
    fun get(): String
}

class InterfacesImpl : `<startingInterface`, `middle<Interface`, normalInterface {
    override fun get(): String = "impl"
}

// IrClass: objects
object `<startingObject` {
    val value: String = "<startingObject"
}
object `middle<Object` {
    val value: String = "middle<Object"
}
object normalObject {
    val value: String = "normalObject"
}

// IrClass: annotation classes
annotation class `<startingAnnotation`
annotation class `middle<Annotation`
annotation class normalAnnotation

@`<startingAnnotation`
@`middle<Annotation`
@normalAnnotation
fun annotated(): String = "annotated"

// IrClass: enum classes, IrEnumEntry: enum entries
enum class `<startingEnum` { A }
enum class `middle<Enum` { A }
enum class normalEnum {
    `middle<Entry`,
    normalEntry,
}

// IrClass: nested, inner classes and companion objects
class OuterStarting {
    class `<startingNested` {
        val value: String = "nested"
    }

    inner class `<startingInner` {
        val value: String = "inner"
    }

    companion object `<startingCompanion` {
        val value: String = "companion"
    }
}

class OuterMiddle {
    class `middle<Nested` {
        val value: String = "nested"
    }

    inner class `middle<Inner` {
        val value: String = "inner"
    }

    companion object `middle<Companion` {
        val value: String = "companion"
    }
}

class OuterNormal {
    class normalNested {
        val value: String = "nested"
    }

    inner class normalInner {
        val value: String = "inner"
    }

    companion object normalCompanion {
        val value: String = "companion"
    }
}

// IrSimpleFunction: top-level functions
fun `middle<Function`(): String = "middle<Function"
fun normalFunction(): String = "normalFunction"

// IrSimpleFunction: extension functions
fun String.`middle<ExtensionFunction`(): String = this + "m"
fun String.normalExtensionFunction(): String = this + "n"

// IrProperty: top-level properties
val `middle<Val`: String = "middle<Val"
val normalVal: String = "normalVal"

var `middle<Var`: String = ""
var normalVar: String = ""

// IrProperty: extension properties
val String.`middle<ExtensionProperty`: String
    get() = this + "m"
val String.normalExtensionProperty: String
    get() = this + "n"

// IrSimpleFunction, IrProperty: members
class Members {
    fun `middle<MemberFunction`(): String = "middle<MemberFunction"
    fun normalMemberFunction(): String = "normalMemberFunction"

    val `middle<MemberVal`: String = "middle<MemberVal"
    val normalMemberVal: String = "normalMemberVal"

    var `middle<MemberVar`: String = ""
    var normalMemberVar: String = ""
}

object Constants {
    const val `middle<Const`: String = "middle<Const"
    const val normalConst: String = "normalConst"
}

// IrTypeAlias
typealias `middle<Alias` = normalClass
typealias normalAlias = normalClass

typealias `middle<GenericAlias`<T> = List<T>
typealias normalGenericAlias<T> = List<T>

// IrTypeParameter
fun <`<startingT`, `middle<T`, normalT> triple(a: `<startingT`, b: `middle<T`, c: normalT): Triple<`<startingT`, `middle<T`, normalT> =
    Triple(a, b, c)

class Generic<`<startingT`, `middle<T`, normalT>(val a: `<startingT`, val b: `middle<T`, val c: normalT)

inline fun <reified `<startingR`> isStarting(x: Any): Boolean = x is `<startingR`
inline fun <reified `middle<R`> isMiddle(x: Any): Boolean = x is `middle<R`
inline fun <reified normalR> isNormal(x: Any): Boolean = x is normalR

// IrValueParameter
fun concat(`middle<Param`: String, normalParam: String = "n"): String =
    `middle<Param` + normalParam

class WithParams(`middle<CtorParam`: String, normalCtorParam: String) {
    val combined: String = `middle<CtorParam` + normalCtorParam
}

// MODULE: main(lib)
// FILE: main.kt
package main

import lib.*

fun box(): String {
    // IrClass: classes
    if (`<startingClass`("s").value != "s") return "Fail <startingClass"
    if (`middle<Class`("m").value != "m") return "Fail middle<Class"
    if (normalClass("n").value != "n") return "Fail normalClass"

    // IrClass: interfaces
    val impl = InterfacesImpl()
    if ((impl as `<startingInterface`).get() != "impl") return "Fail <startingInterface"
    if ((impl as `middle<Interface`).get() != "impl") return "Fail middle<Interface"
    if ((impl as normalInterface).get() != "impl") return "Fail normalInterface"

    // IrClass: objects
    if (`<startingObject`.value != "<startingObject") return "Fail <startingObject"
    if (`middle<Object`.value != "middle<Object") return "Fail middle<Object"
    if (normalObject.value != "normalObject") return "Fail normalObject"

    // IrClass: annotation classes
    if (annotated() != "annotated") return "Fail annotation classes"

    // IrClass: enum classes, IrEnumEntry: enum entries
    if (`<startingEnum`.A.name != "A") return "Fail <startingEnum"
    if (`middle<Enum`.A.name != "A") return "Fail middle<Enum"
    if (normalEnum.entries.map { it.name } != listOf("middle<Entry", "normalEntry")) return "Fail enum entries: ${normalEnum.entries}"
    if (normalEnum.valueOf("middle<Entry") != normalEnum.`middle<Entry`) return "Fail middle<Entry"
    if (normalEnum.valueOf("normalEntry") != normalEnum.normalEntry) return "Fail normalEntry"
    val entry: normalEnum = normalEnum.`middle<Entry`
    val whenResult = when (entry) {
        normalEnum.`middle<Entry` -> "m"
        normalEnum.normalEntry -> "n"
    }
    if (whenResult != "m") return "Fail enum when: $whenResult"

    // IrClass: nested, inner classes and companion objects
    if (OuterStarting.`<startingNested`().value != "nested") return "Fail <startingNested"
    if (OuterStarting().`<startingInner`().value != "inner") return "Fail <startingInner"
    if (OuterStarting.value != "companion") return "Fail <startingCompanion"
    if (OuterStarting.`<startingCompanion`.value != "companion") return "Fail <startingCompanion by name"
    if (OuterMiddle.`middle<Nested`().value != "nested") return "Fail middle<Nested"
    if (OuterMiddle().`middle<Inner`().value != "inner") return "Fail middle<Inner"
    if (OuterMiddle.value != "companion") return "Fail middle<Companion"
    if (OuterMiddle.`middle<Companion`.value != "companion") return "Fail middle<Companion by name"
    if (OuterNormal.normalNested().value != "nested") return "Fail normalNested"
    if (OuterNormal().normalInner().value != "inner") return "Fail normalInner"
    if (OuterNormal.value != "companion") return "Fail normalCompanion"
    if (OuterNormal.normalCompanion.value != "companion") return "Fail normalCompanion by name"

    // IrSimpleFunction: top-level functions
    if (`middle<Function`() != "middle<Function") return "Fail middle<Function"
    if (normalFunction() != "normalFunction") return "Fail normalFunction"

    // IrSimpleFunction: extension functions
    if ("a".`middle<ExtensionFunction`() != "am") return "Fail middle<ExtensionFunction"
    if ("a".normalExtensionFunction() != "an") return "Fail normalExtensionFunction"

    // IrProperty: top-level properties
    if (`middle<Val` != "middle<Val") return "Fail middle<Val"
    if (normalVal != "normalVal") return "Fail normalVal"
    `middle<Var` = "m"
    normalVar = "n"
    if (`middle<Var` != "m") return "Fail middle<Var: ${`middle<Var`}"
    if (normalVar != "n") return "Fail normalVar: $normalVar"

    // IrProperty: extension properties
    if ("a".`middle<ExtensionProperty` != "am") return "Fail middle<ExtensionProperty"
    if ("a".normalExtensionProperty != "an") return "Fail normalExtensionProperty"

    // IrSimpleFunction, IrProperty: members
    val members = Members()
    if (members.`middle<MemberFunction`() != "middle<MemberFunction") return "Fail middle<MemberFunction"
    if (members.normalMemberFunction() != "normalMemberFunction") return "Fail normalMemberFunction"
    if (members.`middle<MemberVal` != "middle<MemberVal") return "Fail middle<MemberVal"
    if (members.normalMemberVal != "normalMemberVal") return "Fail normalMemberVal"
    members.`middle<MemberVar` = "m"
    members.normalMemberVar = "n"
    if (members.`middle<MemberVar` != "m") return "Fail middle<MemberVar"
    if (members.normalMemberVar != "n") return "Fail normalMemberVar"
    if (Constants.`middle<Const` != "middle<Const") return "Fail middle<Const"
    if (Constants.normalConst != "normalConst") return "Fail normalConst"

    // IrTypeAlias
    val middleAlias: `middle<Alias` = `middle<Alias`("m")
    val normalAliasValue: normalAlias = normalAlias("n")
    if (middleAlias.value + normalAliasValue.value != "mn") return "Fail type aliases"
    val middleList: `middle<GenericAlias`<Int> = listOf(2)
    val normalList: normalGenericAlias<Int> = listOf(3)
    if (middleList + normalList != listOf(2, 3)) return "Fail generic type aliases"

    // IrTypeParameter
    if (triple("s", 1, 'n') != Triple("s", 1, 'n')) return "Fail type parameters of function"
    val generic = Generic("s", 1, 'n')
    if (generic.a != "s" || generic.b != 1 || generic.c != 'n') return "Fail type parameters of class"
    if (!isStarting<String>("s") || isStarting<Int>("s")) return "Fail reified <startingR"
    if (!isMiddle<String>("s") || isMiddle<Int>("s")) return "Fail reified middle<R"
    if (!isNormal<String>("s") || isNormal<Int>("s")) return "Fail reified normalR"

    // IrValueParameter
    if (concat(`middle<Param` = "m") != "mn") return "Fail value parameters"
    if (concat(normalParam = "N", `middle<Param` = "m") != "mN") return "Fail named value parameters"
    val withParams = WithParams(normalCtorParam = "n", `middle<CtorParam` = "m")
    if (withParams.combined != "mn") return "Fail constructor parameters: ${withParams.combined}"
    val lambda = { `<startingLambdaParam`: String, `middle<LambdaParam`: String, normalLambdaParam: String ->
        `<startingLambdaParam` + `middle<LambdaParam` + normalLambdaParam
    }
    if (lambda("s", "m", "n") != "smn") return "Fail lambda parameters"

    // Local IrClass and IrSimpleFunction
    class `<startingLocalClass`(val value: String)
    class `middle<LocalClass`(val value: String)
    class normalLocalClass(val value: String)
    fun `<startingLocalFunction`(): String = "s"
    fun `middle<LocalFunction`(): String = "m"
    fun normalLocalFunction(): String = "n"
    val localClasses = `<startingLocalClass`(`<startingLocalFunction`()).value +
            `middle<LocalClass`(`middle<LocalFunction`()).value +
            normalLocalClass(normalLocalFunction()).value
    if (localClasses != "smn") return "Fail local classes and functions: $localClasses"

    // IrVariable
    val `<startingLocalVal` = "s"
    val `middle<LocalVal` = "m"
    val normalLocalVal = "n"
    if (`<startingLocalVal` + `middle<LocalVal` + normalLocalVal != "smn") return "Fail local variables"
    val (`<startingDestructured`, `middle<Destructured`, normalDestructured) = Triple("s", "m", "n")
    if (`<startingDestructured` + `middle<Destructured` + normalDestructured != "smn") return "Fail destructuring"

    // IrLocalDelegatedProperty
    val `<startingDelegated` by lazy { "s" }
    val `middle<Delegated` by lazy { "m" }
    val normalDelegated by lazy { "n" }
    if (`<startingDelegated` + `middle<Delegated` + normalDelegated != "smn") return "Fail local delegated properties"
    
    return "OK"
} 
