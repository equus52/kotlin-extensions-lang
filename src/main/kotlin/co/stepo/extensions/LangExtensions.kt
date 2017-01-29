package co.stepo.extensions

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.time.LocalDate
import java.util.*
import java.util.Collection
import java.util.stream.Collectors
import kotlin.reflect.companionObjectInstance

fun Number.padStart(length: Int, padChar: Char = ' '): String = this.toString().padStart(length, padChar)
fun Number.padEnd(length: Int, padChar: Char = ' '): String = this.toString().padEnd(length, padChar)

val Double.point2: String get() {
    if (this.isInfinite() || this.isNaN()) return this.toString()
    return BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP).toPlainString()
}

//
// Collection
//

inline fun <T> Iterable<T>.peek(action: (T) -> Unit): List<T> {
    this.forEach(action)
    return this.toList()
}

inline fun <T> Iterable<T>.parallelForEach(crossinline action: (T) -> Unit): Unit {
    val collection = this as Collection<T>
    return collection.parallelStream().forEach { action(it) }
}

inline fun <T, R> Iterable<T>.parallelMap(crossinline transform: (T) -> R): List<R> {
    val collection = this as Collection<T>
    return collection.parallelStream().map { transform(it) }.collect(Collectors.toList<R>())
}

inline fun <T, R> Iterable<T>.parallelFlatMap(crossinline transform: (T) -> Iterable<R>): List<R> {
    val collection = this as Collection<T>
    return collection.parallelStream().flatMap {
        val list = transform(it) as Collection<R>
        list.stream()
    }.collect(Collectors.toList<R>())
}

fun Int.range() = 1..this

inline fun <T, R> Iterable<T>.flatMapForNullable(crossinline transform: (T) -> R?): List<R> = this.flatMap { transform(it)?.let { listOf(it) } ?: listOf() }

inline fun <K, V, R> Map<K, V>.map(transform: (K, V) -> R): List<R> = this.map { entry -> transform(entry.key, entry.value) }


//
// String
//

fun String.substringByByte(charset: Charset, startByteIndex: Int, endByteIndex: Int): String {
    val bytes = this.toByteArray(charset).filterIndexed { i, byte -> i >= startByteIndex && i < endByteIndex }.toByteArray()
    return String(bytes, charset)
}

fun String.splitToLines(): List<String> = this.split("\r\n|[\n\r\u2028\u2029\u0085]".toRegex())


val Charsets.SJIS: Charset
    get() = Charset.forName("SJIS")

//
// Function
//

fun <R> (() -> R).memoize(): () -> R {
    var cache: R? = null
    return {
        cache ?: synchronized(this) {
            cache ?: let {
                val value = this()
                cache = value
                value
            }
        }
    }
}

fun <T, R> ((T) -> R).memoize(): (T) -> R {
    val cache: MutableMap<T, R> = hashMapOf()
    return { cache[it] ?: synchronized(this) { cache.getOrPut(it, { this(it) }) } }
}

fun <A, B, R> ((A, B) -> R).memoize(): (A, B) -> R {
    val cache: MutableMap<Pair<A, B>, R> = hashMapOf()
    return { a: A, b: B ->
        val key = Pair(a, b)
        cache[key] ?: synchronized(this) { cache.getOrPut(key, { this(a, b) }) }
    }
}

fun <A, B, C, R> ((A, B, C) -> R).memoize(): (A, B, C) -> R {
    val cache: MutableMap<Triple<A, B, C>, R> = hashMapOf()
    return { a: A, b: B, c: C ->
        val key = Triple(a, b, c)
        cache[key] ?: synchronized(this) { cache.getOrPut(key, { this(a, b, c) }) }
    }
}

//
// File
//

fun <R> File.mapLine(charset: Charset = Charsets.UTF_8, transform: (String) -> R): List<R> {
    val result = ArrayList<R>()
    forEachLine(charset) { result.add(transform(it)); }
    return result
}


//
// Date
//

fun LocalDate.isInRange(from: LocalDate, to: LocalDate): Boolean {
    return (this.isAfter(from) || this.isEqual(from)) && (this.isBefore(to) || this.isEqual(to))
}


fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObjectInstance?.javaClass == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}


// Math

fun Int.abs(): Int = Math.abs(this)
fun Double.abs(): Double = Math.abs(this)

fun Double.power(n: Int): Double {
    if (n < -999999999 || n > 999999999) {
        throw RuntimeException("Unsupported argument power value: " + n)
    }

    val absN = n.abs()
    var power: Double
    val unit = 4
    if (absN <= unit) {
        power = 1.0
        (1..absN).forEach { power *= this }
    } else {
        power = this.power(absN / unit)
        power = power.power(unit)
        power *= this.power(absN % unit)
    }
    if (power == java.lang.Double.POSITIVE_INFINITY || power == java.lang.Double.NEGATIVE_INFINITY) {
        throw RuntimeException("reached double infinity. power with value:$this, n:$n")
    }

    return if (0 <= n) {
        power
    } else {
        1.0 / power
    }
}

fun Iterable<Double>.averageOrNull(): Double? {
    return if (this.count() == 0) {
        null
    } else {
        this.average()
    }
}