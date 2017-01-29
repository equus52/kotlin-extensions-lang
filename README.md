# kotlin-extensions-lang
Kotlin extensions for general use and for logger(slf4j).

## Usage

Please copy and use the source code like a snippet.

## Example

Collection
```kotlin
list.peek { logger.debug(it) }.map { ... }
```
```kotlin
list.parallelForEach { ... }
```

Function
```kotlin
val cachedMap: (Int) -> Value = { code: Int -> ... }.memoize()
```


Logger
```kotlin
val logger = logger()
```

```kotlin
logger.debug { "This message is not evaluated unless debug enabled = ${logger.isDebugEnabled}" }
```

```kotlin
logger.debugForList { createMessageList() }
```
