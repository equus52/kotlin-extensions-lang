# kotlin-extensions-lang
Kotlin extensions for general use and for logger(slf4j).

## Usage

Please copy and use the source code like a snippet.

## Example

```kotlin
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



//
// Function
//

fun <T, R> ((T) -> R).memoize(): (T) -> R {
    val cache: MutableMap<T, R> = hashMapOf()
    return { cache[it] ?: synchronized(this) { cache.getOrPut(it, { this(it) }) } }
}

// ...
```