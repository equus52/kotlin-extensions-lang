package co.stepo.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun Logger.error(message: () -> String) {
    if (this.isErrorEnabled) {
        this.error(message())
    }
}

inline fun Logger.errorForList(messages: () -> List<String>) {
    if (this.isErrorEnabled) {
        messages().forEach { this.error(it) }
    }
}

inline fun Logger.warn(message: () -> String) {
    if (this.isWarnEnabled) {
        this.warn(message())
    }
}

inline fun Logger.warnForList(messages: () -> List<String>) {
    if (this.isWarnEnabled) {
        messages().forEach { this.warn(it) }
    }
}

inline fun Logger.info(message: () -> String) {
    if (this.isInfoEnabled) {
        this.info(message())
    }
}

inline fun Logger.infoForList(messages: () -> List<String>) {
    if (this.isInfoEnabled) {
        messages().forEach { this.info(it) }
    }
}

inline fun Logger.debug(message: () -> String) {
    if (this.isDebugEnabled) {
        this.debug(message())
    }
}

inline fun Logger.debugForList(messages: () -> List<String>) {
    if (this.isDebugEnabled) {
        messages().forEach { this.debug(it) }
    }
}

inline fun Logger.trace(message: () -> String) {
    if (this.isTraceEnabled) {
        this.trace(message())
    }
}

inline fun Logger.traceForList(messages: () -> List<String>) {
    if (this.isTraceEnabled) {
        messages().forEach { this.trace(it) }
    }
}

fun <R : Any> R.logger(): Logger {
    return LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name)
}

fun loggerFor(clazz: Class<*>): Logger {
    return LoggerFactory.getLogger(unwrapCompanionClass(clazz).name)
}

fun loggerFor(name: String): Logger {
    return LoggerFactory.getLogger(name)
}